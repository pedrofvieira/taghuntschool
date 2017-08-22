
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

package br.cin.ufpe.taghuntschool.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.springframework.stereotype.Repository;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;

@Repository
@Named
@RequestScoped
public class GeneralUsrDAO extends GenericDAO<GeneralUsr> {
	
	
	public GeneralUsrDAO() {
        super(GeneralUsr.class);
    }
	

	public GeneralUsr findByUsername(String username) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("username", username);     
 
        List<GeneralUsr> users = super.findResultsByParameter(GeneralUsr.FIND_BY_USERNAME, parameters);
        if(!users.isEmpty()){
        	return users.get(0);
        }
        return null;
	}

	public GeneralUsr findByEmail(String email) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("email", email);     
 
        List<GeneralUsr> users = super.findResultsByParameter(GeneralUsr.FIND_BY_EMAIL, parameters);
        if(!users.isEmpty()){
        	return users.get(0);
        }
        return null;
	}
	
	public List<GeneralUsr> findAllInstructors(){
 
        List<GeneralUsr> users = super.findResultsByParameter(GeneralUsr.FIND_ALL_INSTRUCTORS, null);
        if(!users.isEmpty()){
        	return users;
        }
        return null;
	}

	public List<GeneralUsr> findInstructorLikeName(String searchName){
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("searchName", "%"+searchName.trim()+"%");     
 
        List<GeneralUsr> users = super.findResultsByParameter(GeneralUsr.FIND_INSTRUCTOR_LIKE_NAME, parameters);
        if(!users.isEmpty()){
        	return users;
        }
        return null;
	}

	public GeneralUsr findAllStudentByKey(String sessionKey){

		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("sessionKey", sessionKey);     

        List<GeneralUsr> users = super.findResultsByParameter(GeneralUsr.FIND_ALL_STUDENT_BY_KEY, parameters);
        if(!users.isEmpty()){
        	return users.get(0);
        }
        return null;
	}

}
