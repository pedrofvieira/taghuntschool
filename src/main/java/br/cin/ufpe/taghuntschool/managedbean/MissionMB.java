
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.FlowEvent;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.domain.Mission;
import br.cin.ufpe.taghuntschool.domain.MissionStep;
import br.cin.ufpe.taghuntschool.domain.StudentMission;
import br.cin.ufpe.taghuntschool.service.GeneralUsrService;
import br.cin.ufpe.taghuntschool.service.MissionService;
import br.cin.ufpe.taghuntschool.service.MissionStepService;
import br.cin.ufpe.taghuntschool.service.StudentMissionService;
import br.cin.ufpe.taghuntschool.util.FacesContextUtil;
import br.cin.ufpe.taghuntschool.util.SecurityUtil;
import br.cin.ufpe.taghuntschool.util.Utils;

@ManagedBean
@ViewScoped
public class MissionMB implements Serializable {

	private static final long serialVersionUID = -4705946887970623931L;

	private GeneralUsr instructor;
	
    private Mission missionEdited;
    
	private boolean newRecord;
	
	private boolean newRecordMissionStep;
	
	private MissionStep missionStepEdited;
	
	private List<Mission> missionByInstructorList; 
	
	private List<StudentMission> studentMissionList; 
	
    @Inject
	private GeneralUsrService generalUsrService;

    @Inject
	private MissionService missionService;
    
    @Inject
    private MissionStepService missionStepService;
	
    @Inject
	private StudentMissionService studentMissionService;
    
	public GeneralUsr getInstructor() {
		return instructor;
	}

	public void setInstructor(GeneralUsr instructor) {
		this.instructor = instructor;
	}

	public Mission getMissionEdited() {
		return missionEdited;
	}

	public void setMissionEdited(Mission missionEdited) {
		this.missionEdited = missionEdited;
	}

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

	public boolean isNewRecordMissionStep() {
		return newRecordMissionStep;
	}

	public void setNewRecordMissionStep(boolean newRecordMissionStep) {
		this.newRecordMissionStep = newRecordMissionStep;
	}

	public MissionStep getMissionStepEdited() {
		return missionStepEdited;
	}

	public void setMissionStepEdited(MissionStep missionStepEdited) {
		this.missionStepEdited = missionStepEdited;
	}

	public List<Mission> getMissionByInstructorList() {
		return missionByInstructorList;
	}

	public void setMissionByInstructorList(List<Mission> missionByInstructorList) {
		this.missionByInstructorList = missionByInstructorList;
	}

    public List<StudentMission> getStudentMissionList() {
		return studentMissionList;
	}

	public void setStudentMissionList(List<StudentMission> studentMissionList) {
		this.studentMissionList = studentMissionList;
	}

	@PostConstruct
    public void init() {
		try {
			
			if(this.instructor == null){
				this.instructor = (GeneralUsr) FacesContextUtil.getAttributeHttpSession("usuarioLogado");
			}
			if(this.instructor ==  null || this.instructor.getId() == null){
			
				Principal principal = FacesContextUtil.getFacesContextCurrent().getExternalContext().getUserPrincipal();		
				if(principal == null){
					// Usuário não logado
				} else {
					this.instructor = generalUsrService.loadUserByUsername(principal.getName());
					FacesContextUtil.setAttributeHttpSession("usuarioLogado", this.instructor);
				}
			}
			if(this.missionEdited == null){
				this.missionEdited = (Mission) FacesContextUtil.getAttributeHttpSession("missionEdited");
			}
			if(this.missionEdited == null){
				resetMissionEdited();
				setNewRecord(true);
			} else {
				setNewRecord(false);
			}
			if(this.missionStepEdited == null){
				this.missionStepEdited = (MissionStep) FacesContextUtil.getAttributeHttpSession("missionStepEdited");
			}
			if(this.missionStepEdited == null){
				resetMissionStepEdited();
				setNewRecordMissionStep(true);
			} else {
				setNewRecordMissionStep(false);
			}
			missionByInstructorList = new ArrayList<Mission>();
			loadMissionByInstructor();
			this.studentMissionList = new ArrayList<StudentMission>();
			if(!this.isNewRecord() && this.getMissionEdited() != null){
				loadStudentMissionByMission();
			}
		} catch (Exception e) {
	    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
	        e.printStackTrace();
		}
	}

    public String prepareSearch(){
		if(FacesContextUtil.isUserInRole("ROLE_INSTRUCTOR")){
			return Utils.FIND_MISSION;
		}
		FacesContextUtil.setMensagemErro("Acesso negado!");
		return null;
    }

    public String prepareCreate(){
		FacesContextUtil.setAttributeHttpSession("missionEdited", null);
		return Utils.EDIT_MISSION;
    }
    
    public String prepareEdit(Mission mission){
    	this.missionEdited = mission;
		FacesContextUtil.setAttributeHttpSession("missionEdited", this.missionEdited);
		
		return Utils.EDIT_MISSION;
    }
   
    public String prepareManageMissionSteps(Mission mission){
		if(FacesContextUtil.isUserInRole("ROLE_INSTRUCTOR")){
 			
 			this.missionEdited = mission;
 			FacesContextUtil.setAttributeHttpSession("missionEdited", this.missionEdited);
 			
 			return Utils.MANAGE_MISSION_STEPS;
 		}
 		FacesContextUtil.setMensagemErro("Acesso negado!");
 		return null;
    }
    
    public String prepareViewDetail(Mission mission){
    	this.missionEdited = mission;
		FacesContextUtil.setAttributeHttpSession("missionEdited", this.missionEdited);
		
		return Utils.DETAIL_MISSION;
    }
   
    public String returnManageMissionSteps(){
		FacesContextUtil.setAttributeHttpSession("missionStepEdited", null);
 		return Utils.MANAGE_MISSION_STEPS;
    }
    
    public String prepareCreateMissionStep(){
		if(FacesContextUtil.isUserInRole("ROLE_INSTRUCTOR")){
 			
 			return Utils.EDIT_MISSION_STEPS;
 		}
 		FacesContextUtil.setMensagemErro("Acesso negado!");
 		return null;
    }
    
    public String prepareEditMissionSteps(MissionStep missionStepEdited){
		if(FacesContextUtil.isUserInRole("ROLE_INSTRUCTOR")){
 			
 			this.missionStepEdited = missionStepEdited;
 			FacesContextUtil.setAttributeHttpSession("missionStepEdited", this.missionStepEdited);
 			
 			return Utils.EDIT_MISSION_STEPS;
 		}
 		FacesContextUtil.setMensagemErro("Acesso negado!");
 		return null;
     }

	public String registerMission() {
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				missionService.create(this.missionEdited);
				
				FacesContextUtil.setMensagemNormal("O cadastro da missão foi realizado com sucesso.");
				
				FacesContextUtil.setAttributeHttpSession("missionEdited", null);
				return Utils.FIND_MISSION;
	        } catch (Exception e) {
		    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
		return null;
	}
    
	public String updateMission(){
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {

				setMissionEdited(missionService.update(this.missionEdited));

				FacesContextUtil.setMensagemNormal("A atualização do cadastro da missão foi realizada com sucesso.");
				
				FacesContextUtil.setAttributeHttpSession("missionEdited", null);
				return Utils.FIND_MISSION;
	        } catch (Exception e) {
		    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
		return null;
	}
    
	public String addMissionStep() {
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				this.missionStepEdited.setThingKey(SecurityUtil.geraToken60URL());
				this.missionEdited.addMissionStep(this.missionStepEdited);
				setMissionEdited(missionService.update(this.missionEdited));
				FacesContextUtil.setMensagemNormal("O novo passo da missão foi adicionado com sucesso.");
				FacesContextUtil.setAttributeHttpSession("missionStepEdited", null);
				FacesContextUtil.setAttributeHttpSession("missionEdited", this.missionEdited);
				return Utils.MANAGE_MISSION_STEPS;
	        } catch (Exception e) {
		    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
		return null;
	}

	public String updateMissionStep() {
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				this.missionEdited.updateMissionStep(this.missionStepEdited);
				setMissionEdited(missionService.update(this.missionEdited));
				FacesContextUtil.setMensagemNormal("O passo da missão foi atualizado com sucesso.");
				FacesContextUtil.setAttributeHttpSession("missionStepEdited", null);
				FacesContextUtil.setAttributeHttpSession("missionEdited", this.missionEdited);
				return Utils.MANAGE_MISSION_STEPS;
	        } catch (Exception e) {
		    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
		return null;
	}
    
	private void loadMissionByInstructor(){
		try {
			this.missionByInstructorList = missionService.findAllByInstructor(this.instructor);
        } catch (Exception e) {
	    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
            e.printStackTrace();
		}
	}
	
	private void loadStudentMissionByMission(){
		try {
			this.studentMissionList = studentMissionService.findByMission(this.getMissionEdited());
        } catch (Exception e) {
	    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
            e.printStackTrace();
		}
	}
	
	public void resetMissionEdited(){
		this.missionEdited = new Mission(this.instructor);
		this.missionEdited.setPublished(false);
		this.missionEdited.setSteps(new ArrayList<MissionStep>());
	}

	public void resetMissionStepEdited(){
		this.missionStepEdited = new MissionStep();
	}
    
	private Boolean missionValidate(){
		Mission missionExists = null;
		Boolean ret = true;
		
		try {
			missionExists = missionService.findByTitle(this.missionEdited.getTitle());
        } catch (Exception e) {
	    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
            e.printStackTrace();
		}
		if(missionExists == null || missionExists.getId().equals(this.missionEdited.getId())){
			ret = true;
		} else {
			FacesContextUtil.setMensagemErro("Existe outra missão com mesmo título.");
			ret = false;
		}

		return ret;
	}
	
	private Boolean missionStepValidate(){
		MissionStep missionStepExists = null;
		Boolean ret = true;

		try {
			missionStepExists = missionStepService.findByThingName(this.missionStepEdited.getThingName());
			
			if(missionStepExists == null || missionStepExists.getId().equals(this.missionStepEdited.getId())){
				ret = true;
			} else {
				FacesContextUtil.setMensagemErro("Existe outro objeto com mesmo nome.");
				ret = false;
			}
        } catch (Exception e) {
        	return false;
        }
		return ret;
	}
	
	public String onFlowProcess(FlowEvent event) {
    	if(this.missionEdited.getTitle() != null){
           	if(!missionValidate()){
        		return event.getOldStep();
         	}
    	}
        return event.getNewStep();
	}
	
	public String onFlowProcessMissionStep(FlowEvent event) {
    	if(this.missionStepEdited.getThingName() != null){
           	if(!missionStepValidate()){
        		return event.getOldStep();
         	}
    	}
        return event.getNewStep();
	}
}
