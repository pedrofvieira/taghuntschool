
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

package br.cin.ufpe.taghuntschool.service;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cin.ufpe.taghuntschool.dao.MissionDAO;
import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.domain.Mission;
import br.cin.ufpe.taghuntschool.exception.ExceptionTaghuntSchool;

@Service
@Named
@RequestScoped
public class MissionService {
	
	@Autowired
	@Inject
	MissionDAO missionDAO;

	public List<Mission> findAll(){
		return missionDAO.findAll();
	}
	
	public Mission findByTitle(String title) {
		return missionDAO.findByTitle(title);
	}
	
	public List<Mission> findAllByInstructor(GeneralUsr instructor) {
		return missionDAO.findAllByInstructor(instructor);
	}
	
	public List<Mission> findByThingKey(String thingKey) {
		return missionDAO.findByThingKey(thingKey);
	}
	
	public Mission findPublishedByThingKey(String thingKey) {
		return missionDAO.findPublishedByThingKey(thingKey);
	}
	
	public List<Mission> findAllPublished() {
		return missionDAO.findAllPublished();
	}
	
	public Map<String, Number> getMissionStats(){
		return missionDAO.getMissionStats();
	}

	public Map<String, Number> getMissionStats(GeneralUsr instructor){
		return missionDAO.getMissionStats(instructor);
	}

	@Transactional
    public Mission update(Mission mission) throws ExceptionTaghuntSchool{
		return missionDAO.update(mission);
	}
	
	@Transactional
    public void create(Mission mission) throws ExceptionTaghuntSchool{
		missionDAO.update(mission);
	}
}
