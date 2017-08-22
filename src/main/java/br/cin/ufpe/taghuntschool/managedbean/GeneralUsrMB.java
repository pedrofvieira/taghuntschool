
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
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FlowEvent;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.exception.ExceptionTaghuntSchool;
import br.cin.ufpe.taghuntschool.service.GeneralUsrService;
import br.cin.ufpe.taghuntschool.util.Utils;
import br.cin.ufpe.taghuntschool.util.FacesContextUtil;

import javax.inject.Inject;

@ManagedBean
@ViewScoped
public class GeneralUsrMB implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private GeneralUsr instructorEdited;
	
	private boolean newRecord;
	
	private List<GeneralUsr> instructorList; 
	
    private String searchName;
	
    @Inject
	private GeneralUsrService generalUsrService;

	public GeneralUsr getInstructorEdited() {
		return instructorEdited;
	}

	public void setInstructorEdited(GeneralUsr instructorEdited) {
		this.instructorEdited = instructorEdited;
	}

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}

	public List<GeneralUsr> getInstructorList() {
		return instructorList;
	}

	public void setInstructorList(List<GeneralUsr> instructorList) {
		this.instructorList = instructorList;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	@PostConstruct
    public void init() {
		try {
			if(this.instructorEdited == null){
				this.instructorEdited = (GeneralUsr) FacesContextUtil.getAttributeHttpSession("instructorEdited");
			}
			if(this.instructorEdited ==  null || this.instructorEdited.getId() == null){
				resetInstructorEdited();
				setNewRecord(true);
			} else {
				setNewRecord(false);
			}
			instructorList = new ArrayList<GeneralUsr>();
			loadInstructors();
			this.searchName = null;
		} catch (Exception e) {
			FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
	        e.printStackTrace();
		}
	}
    
    public String prepareSearchInstructor(){
		if(FacesContextUtil.isUserInRole("ROLE_ADMIN")){
			FacesContextUtil.setAttributeHttpSession("instructorEdited", null);
			return Utils.FIND_INSTRUCTOR;
		}
		FacesContextUtil.setMensagemErro("Acesso negado!");
		return null;
    }
    
    public String prepareCreate(){
    	return Utils.EDIT_INSTRUCTOR;
    }
    
    public String prepareEdit(GeneralUsr generalUsr){
    	this.instructorEdited = generalUsr;
    	FacesContextUtil.setAttributeHttpSession("instructorEdited", this.instructorEdited);
    	return Utils.EDIT_INSTRUCTOR;
    }

	public void resetInstructorEdited(){
		this.instructorEdited = new GeneralUsr();
		this.instructorEdited.setEnabled(true);
	}

	public void searchInstructors(){
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
			try {
				if(this.searchName == null || this.searchName.trim().equals("")){
					this.instructorList = generalUsrService.findAllInstructors();
				} else {
					this.instructorList = generalUsrService.findInstructorLikeName(this.searchName.toLowerCase().trim());
					
					if(this.instructorList == null){
						FacesContextUtil.setMensagemNormal("Nenhum registro encontrado.");
					}
				}
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
		}
    }
    
	public String updateInstructor() {
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				generalUsrService.update(this.instructorEdited);

				FacesContextUtil.setMensagemNormal("A atualização do cadastro de usuário foi realizada com sucesso.");
				
		    	FacesContextUtil.setAttributeHttpSession("instructorEdited", null);
				return Utils.FIND_INSTRUCTOR;
			} catch (ExceptionTaghuntSchool e) {
				FacesContextUtil.setMensagemErro(e.getMessage());
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
		return null;
	}
	
	public String registerInstructor() {
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				
				generalUsrService.create(this.instructorEdited);
				
				FacesContextUtil.setMensagemNormal("O cadastro do usuário foi realizado com sucesso.");
				
		    	FacesContextUtil.setAttributeHttpSession("instructorEdited", null);
				return Utils.FIND_INSTRUCTOR;
			} catch (ExceptionTaghuntSchool e) {
				FacesContextUtil.setMensagemErro(e.getMessage());
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
		return null;
	}

	private Boolean instructorValidate(){
		GeneralUsr userExists = null;
		Boolean ret = true;
		
		try {
			userExists = generalUsrService.findByEmail(this.instructorEdited.getEmail());
        } catch (Exception e) {
			FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
            e.printStackTrace();
		}
		if(userExists == null || userExists.getUsername().equals(this.instructorEdited.getUsername())){
			ret = true;
		} else {
			FacesContextUtil.setMensagemErro("Existe um outro usuário com mesmo email.");
			ret = false;
		}

		if(this.instructorEdited.getId() == null){
			try {
				userExists = generalUsrService.loadUserByUsername(this.instructorEdited.getUsername().toLowerCase());
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
			if(userExists != null){
				FacesContextUtil.setMensagemErro("Existe um outro usuário com mesmo login.");
				ret = false;
			}
			if(!this.instructorEdited.getPassword().equals(this.instructorEdited.getPasswordToConfirm())){
				FacesContextUtil.setMensagemErro("As senhas não conferem.");
				ret = false;
			}

			if(!this.instructorEdited.getEmail().equals(this.instructorEdited.getEmailToConfirm())){
				FacesContextUtil.setMensagemErro("Os emails não conferem.");
				ret = false;
			}
		}
		return ret;
	}
	
    public String saveInstructorPassword(){
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				generalUsrService.updatePassword(this.instructorEdited);
				
				FacesContextUtil.setMensagemNormal("A atualização da senha de usuário foi realizada com sucesso!");
				
				return Utils.FIND_INSTRUCTOR;
			} catch (ExceptionTaghuntSchool e) {
				FacesContextUtil.setMensagemErro(e.getMessage());
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
    	return null;
    }
	
	public void loadInstructors(){
		try {
			this.instructorList = generalUsrService.findAllInstructors();
        } catch (Exception e) {
			FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
            e.printStackTrace();
		}
	}
	
	public String onFlowProcess(FlowEvent event) {
		//if(this.instructorEdited.getUsername() != null){
	       	if(!instructorValidate()){
	    		return event.getOldStep();
	     	}
		//}
         return event.getNewStep();
	}

}
