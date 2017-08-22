
/******* 
    Taghunt School - uma plataforma para criação de jogos como ferramenta de apoio à educação
    Copyright (C) 2017 Pedro Fernandes Vieira
 
    Este arquivo é parte da Taghunt School
 
    Taghunt School é um software livre;  você pode redistribuí-lo e/ou modificá-lo sob os
    termos da Licença Pública Geral GNU/GPL como publicada pela Free Software Foundation,
    na versão 3 da Licença, ou (a seu critério) qualquer versão mais recente.
 
    Este programa é distribuído na expectativa de ser útil, mas SEM QUALQUER GARANTIA; sem
    mesmo a garantia implícita de COMERCIALIZAÇÃO ou de ADEQUAÇÃO A QUALQUER APLICAÇÃO 
    EM PARTICULAR. Veja Licença Pública Geral GNU/GPL em português para maiores detalhes.
 
    Você deve ter recebido uma cópia da Licença Pública Geral GNU/GPL, sob o título “LICENSE”,
    junto com Taghunt School. Se não, acesse <http://www.gnu.org/licenses/>. 

    ---
	Taghunt School - Platform for creating games as a tool to support education
    Copyright (C) 2017 Pedro Fernandes Vieira
	
    This file is part of Taghunt School.

    Taghunt School is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Taghunt School is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Taghunt School.  If not, see <http://www.gnu.org/licenses/>.

*******/  

package br.cin.ufpe.taghuntschool.managedbean;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.domain.Mission;
import br.cin.ufpe.taghuntschool.domain.MissionStep;
import br.cin.ufpe.taghuntschool.domain.RoleKind;
import br.cin.ufpe.taghuntschool.domain.StudentMission;
import br.cin.ufpe.taghuntschool.domain.StudentMissionStep;
import br.cin.ufpe.taghuntschool.service.GeneralUsrService;
import br.cin.ufpe.taghuntschool.service.MissionService;
import br.cin.ufpe.taghuntschool.service.MissionStepService;
import br.cin.ufpe.taghuntschool.service.StudentMissionService;
import br.cin.ufpe.taghuntschool.util.FacesContextUtil;
import br.cin.ufpe.taghuntschool.util.SecurityUtil;
import br.cin.ufpe.taghuntschool.util.Utils;

@ManagedBean
@ViewScoped
public class GameControllerMB implements Serializable {

	private static final long serialVersionUID = -2334379530840981145L;

    private String action;

    private String thingKey;
    
    private boolean studentMissionFinished;

    private boolean studentFirstThingOfMission;

	private boolean showNextStep;
    
	private boolean missionFoundedOK;
    
	private boolean studentMissionFoundedOK;
	
	private String welcomeMessage;

	private String messageOutput;
	
	private Mission missionFounded;
	
	private MissionStep missionStepFounded;
	
	private GeneralUsr student;
	
	private StudentMissionStep nextStudentMissionStep;

    @Inject
	private GeneralUsrService generalUsrService;

    @Inject
	private MissionService missionService;

    @Inject
    private MissionStepService missionStepService;
    
    @Inject
    private StudentMissionService studentMissionService;

    @PostConstruct
    public void init() {
    	setMessageOutput("");
    }
    
    public void prepareGameController(){
    	boolean first = false;
    	String sessionKey;
    	StudentMission studentMissionFounded = null;
    	StudentMissionStep studentMissionStepFounded = null;
    	
    	this.setMissionFoundedOK(false);
    	this.setStudentMissionFoundedOK(false);
    	this.setStudentMissionFinished(false);
    	this.setShowNextStep(false);
    	this.setWelcomeMessage("Seja bem-vindo!!!");
    	
		if (this.getAction() == null || this.getThingKey() == null) {
			this.setMessageOutput("Este objeto não faz parte de nenhuma missão cadastrada na Plataforma Taghunt School!");
			return;
		}
		try {

			// Check if not exists thing registered with 'thingkey' param value
			this.setMissionStepFounded(missionStepService.findByThingKey(this.getThingKey()));
			if (this.getMissionStepFounded() == null){
				this.setMessageOutput("Este objeto não faz parte de nenhuma missão cadastrada na Plataforma Taghunt School!");
				return;
			}
			
			this.setMissionFounded(missionService.findPublishedByThingKey(this.getThingKey()));

			if (this.getMissionFounded() == null){
				this.setMessageOutput("Este objeto não faz parte de uma missão publicada na Plataforma Taghunt School!");
				return;
			} else {
		    	this.setMissionFoundedOK(true);
			}
			
			// Check existent cookie and if dont exists then create cookie and user
			sessionKey = FacesContextUtil.getCookieValue(Utils.COOKIE_KEY);
			if(sessionKey != null) {
				// Dont first visit for student (Sessionkey)
		    	
				this.setStudent(generalUsrService.findAllStudentByKey(sessionKey));
				if (this.getStudent() != null){
					this.setWelcomeMessage("Seja bem-vindo de volta " + this.getStudent().getName() + "!");
				}
			}
			
			if(sessionKey == null || this.getStudent() == null){
				first = true;

	    		// Generate new key
				sessionKey = SecurityUtil.newUuidBase64();
	    		// Create new student
				this.setStudent(new GeneralUsr(sessionKey, sessionKey));
				this.getStudent().setPrincipalRole(new RoleKind(Utils.ROLE_STUDENT_ID));
				this.getStudent().setEnabled(false);
	    		generalUsrService.create(this.getStudent());
	    		// Change Name of Student
	    		this.setStudent(generalUsrService.findAllStudentByKey(sessionKey));
	    		this.getStudent().setName("User Anonymous " + this.getStudent().getId().toString());
	    		this.setStudent(generalUsrService.update(this.getStudent()));
	    		// Create cookie
	    		FacesContextUtil.setCookieValue(Utils.COOKIE_KEY, sessionKey);
	    		
	    		FacesContextUtil.setMensagemNormal("Esse sistema utiliza cookies para diferenciar você de outros usuários. Ao continuar navegando pelo site, você está concordando com o nosso uso de cookies.");
				this.setWelcomeMessage("É seu primeiro acesso? Seja bem-vindo " + this.getStudent().getName() + "!");
	    	}	
				
			if(!first && this.getMissionFounded() != null){
				// check if mission student found
				studentMissionFounded = studentMissionService.findByStudentAndMission(this.getStudent(), this.getMissionFounded());
				
				if(studentMissionFounded != null){
					this.setStudentMissionFoundedOK(true);
					
					// Check if the student finished this mission
					if(studentMissionFounded.isFinished()){
						this.setStudentMissionFinished(true);
						return;
					}

					this.setNextStudentMissionStep(studentMissionFounded.findNextStudentMissionStep());
					if(this.getNextStudentMissionStep() != null)
						this.setShowNextStep(true);
					studentMissionStepFounded = studentMissionFounded.getStudentMissionStep(this.getMissionStepFounded());
					
					if(studentMissionStepFounded != null){
						// Check if this thing was visit by the student
						if(studentMissionStepFounded.getComplete() != null){
							if(isStudentFirstThingOfMission()){
								this.setMessageOutput(this.getMessageOutput() + "Veja abaixo a dica para encontrar o objeto do próximo passo da missão! ");
							} else {
								this.setMessageOutput(this.getMessageOutput() + "Você já acessou este objeto antes. Veja abaixo a dica para encontrar o objeto do próximo passo da missão! ");							
							}
							return;
						}
						
						// Check if the thing visited is the thing of next step of student mission
						if(!studentMissionStepFounded.equals(this.getNextStudentMissionStep())){
							this.setMessageOutput(this.getMessageOutput() + "O objeto acessado não é o objeto do próximo passo da missão. Veja abaixo a dica para encontrar o objeto do próximo passo da missão!");
						} else {
							// Register visit and show next step
							studentMissionFounded.completeStudentMissionStep(studentMissionStepFounded);
							
							this.setNextStudentMissionStep(studentMissionFounded.findNextStudentMissionStep());
							if(this.getNextStudentMissionStep() != null){
								this.setThingKey(this.getNextStudentMissionStep().getMissionStep().getThingKey());
								this.setShowNextStep(true);
							} else {
								studentMissionFounded.setFinished(true);
								this.setStudentMissionFinished(true);
								this.setShowNextStep(false);
							}

							studentMissionFounded = studentMissionService.update(studentMissionFounded);
						}
						
					}
										
				} // end of condition = studentMissionFounded != null
			}
		} catch (Exception e) {
			FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
	        e.printStackTrace();
		}
    }
    
    public void prepareNewStudentMission(){
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
			try {
				StudentMission newStudentMission = new StudentMission(this.getMissionFounded(), this.getStudent());
				newStudentMission.setStudentMissionSteps(new ArrayList<StudentMissionStep>());
				newStudentMission.generateOrderedStudentMissionSteps(this.getMissionStepFounded());
				
				studentMissionService.create(newStudentMission);
				
				newStudentMission = studentMissionService.findByStudentAndMission(this.getStudent(), this.getMissionFounded());
				
				this.setNextStudentMissionStep(newStudentMission.findNextStudentMissionStep());
				if(this.getNextStudentMissionStep() != null)
					this.setStudentFirstThingOfMission(true);
					this.setShowNextStep(true);
				
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
		}
    }
    
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getThingKey() {
		return thingKey;
	}

	public void setThingKey(String thingKey) {
		this.thingKey = thingKey;
	}

	public boolean isStudentMissionFinished() {
		return studentMissionFinished;
	}

	public void setStudentMissionFinished(boolean studentMissionFinished) {
		this.studentMissionFinished = studentMissionFinished;
	}

	public boolean isStudentFirstThingOfMission() {
		return studentFirstThingOfMission;
	}

	public void setStudentFirstThingOfMission(boolean studentFirstThingOfMission) {
		this.studentFirstThingOfMission = studentFirstThingOfMission;
	}

	public boolean isShowNextStep() {
		return showNextStep;
	}

	public void setShowNextStep(boolean showNextStep) {
		this.showNextStep = showNextStep;
	}

	public boolean isMissionFoundedOK() {
		return missionFoundedOK;
	}

	public void setMissionFoundedOK(boolean foundedOK) {
		this.missionFoundedOK = foundedOK;
	}

	public boolean isStudentMissionFoundedOK() {
		return studentMissionFoundedOK;
	}

	public void setStudentMissionFoundedOK(boolean studentMissionFoundedOK) {
		this.studentMissionFoundedOK = studentMissionFoundedOK;
	}

	public String getWelcomeMessage() {
		return welcomeMessage;
	}

	public void setWelcomeMessage(String welcomeMessage) {
		this.welcomeMessage = welcomeMessage;
	}

	public String getMessageOutput() {
		return messageOutput;
	}

	public void setMessageOutput(String messageOutput) {
		this.messageOutput = messageOutput;
	}

	public Mission getMissionFounded() {
		return missionFounded;
	}

	public void setMissionFounded(Mission missionFounded) {
		this.missionFounded = missionFounded;
	}

	public MissionStep getMissionStepFounded() {
		return missionStepFounded;
	}

	public void setMissionStepFounded(MissionStep missionStepFounded) {
		this.missionStepFounded = missionStepFounded;
	}

	public GeneralUsr getStudent() {
		return student;
	}

	public void setStudent(GeneralUsr student) {
		this.student = student;
	}

	public StudentMissionStep getNextStudentMissionStep() {
		return nextStudentMissionStep;
	}

	public void setNextStudentMissionStep(StudentMissionStep nextStudentMissionStep) {
		this.nextStudentMissionStep = nextStudentMissionStep;
	}
    
}
