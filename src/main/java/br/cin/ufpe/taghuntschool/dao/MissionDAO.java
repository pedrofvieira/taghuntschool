
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
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.domain.Mission;
import br.cin.ufpe.taghuntschool.util.Utils;

@Repository
@Named
@RequestScoped
public class MissionDAO extends GenericDAO<Mission>{
	
	public MissionDAO(){
		super(Mission.class);
	}

	public Mission findByTitle(String title) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("title", title);     

        List<Mission> missions = super.findResultsByParameter(Mission.FIND_BY_TITLE, parameters);
        if(!missions.isEmpty()){
        	return missions.get(0);
        }
        return null;
	}

	public List<Mission> findAllByInstructor(GeneralUsr instructor) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("instructor", instructor);     

        List<Mission> missions = super.findResultsByParameter(Mission.FIND_ALL_BY_INSTRUCTOR, parameters);
        if(!missions.isEmpty()){
        	return missions;
        }
        return null;
	}

	public List<Mission> findByThingKey(String thingKey) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("thingKey", thingKey);     

        List<Mission> missions = super.findResultsByParameter(Mission.FIND_BY_THING_KEY, parameters);
        if(!missions.isEmpty()){
        	return missions;
        }
        return null;
	}

	public Mission findPublishedByThingKey(String thingKey) {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("thingKey", thingKey);     

        List<Mission> missions = super.findResultsByParameter(Mission.FIND_PUBLISHED_BY_THING_KEY, parameters);
        if(!missions.isEmpty()){
        	return missions.get(0);
        }
        return null;
	}

	public List<Mission> findAllPublished() {
		
        List<Mission> missions = super.findResultsByParameter(Mission.FIND_ALL_PUBLISHED, null);
        if(!missions.isEmpty()){
        	return missions;
        }
        return null;
	}
	
	public Map<String, Number> getMissionStats(){
		Map<String, Number> ret = new HashMap<String, Number>();
		Number count = 0;
		String jpql_sql = "";
		
		count = (Number) this.em.createNamedQuery(Mission.COUNT_ALL).getSingleResult();
		ret.put(Utils.DONUT_CHART_ALL_MISSIONS, count);
		
		count = (Number) this.em.createNamedQuery(Mission.COUNT_ALL_PUBLISHED).getSingleResult();
		ret.put(Utils.DONUT_CHART_PUBLISHED_MISSIONS, count);
		
		jpql_sql = "SELECT count(s.id) FROM StudentMission s";
		count = (Number) this.em.createQuery(jpql_sql).getSingleResult();
		ret.put(Utils.DONUT_CHART_ALL_STUDENT_MISSIONS, count);
		
		jpql_sql = "SELECT count(s.id) FROM StudentMission s WHERE s.finished = true";
		count = (Number) this.em.createQuery(jpql_sql).getSingleResult();
		ret.put(Utils.DONUT_CHART_FINISHED_STUDENT_MISSIONS, count);
		
		jpql_sql = "SELECT count(t.id) FROM MissionStep t";
		count = (Number) this.em.createQuery(jpql_sql).getSingleResult();
		ret.put(Utils.DONUT_CHART_ALL_STEPS, count);
		
		jpql_sql = "SELECT count(s.id) FROM StudentMissionStep s WHERE s.complete IS NOT NULL ";
		count = (Number) this.em.createQuery(jpql_sql).getSingleResult();
		ret.put(Utils.DONUT_CHART_VISITED_MISSION_STEPS, count);
		
		return ret;
	}

	public Map<String, Number> getMissionStats(GeneralUsr instructor){
		Query query;
		Map<String, Number> ret = new HashMap<String, Number>();
		Number count = 0;
		String jpql_sql = "";
		
		query = this.em.createNamedQuery(Mission.COUNT_ALL_BY_INSTRUCTOR);
		query.setParameter("instructor", instructor);
		count = (Number) query.getSingleResult();
		ret.put(Utils.DONUT_CHART_ALL_MISSIONS, count);
		
		query = this.em.createNamedQuery(Mission.COUNT_ALL_PUBLISHED_BY_INSTRUCTOR);
		query.setParameter("instructor", instructor);
		count = (Number) query.getSingleResult();
		ret.put(Utils.DONUT_CHART_PUBLISHED_MISSIONS, count);
		
		jpql_sql = "SELECT count(s.id) FROM StudentMission s WHERE s.mission.instructor = :instructor";
		query = this.em.createQuery(jpql_sql);
		query.setParameter("instructor", instructor);
		count = (Number) query.getSingleResult();
		ret.put(Utils.DONUT_CHART_ALL_STUDENT_MISSIONS, count);
		
		jpql_sql = "SELECT count(s.id) FROM StudentMission s WHERE s.finished = true AND s.mission.instructor = :instructor";
		query = this.em.createQuery(jpql_sql);
		query.setParameter("instructor", instructor);
		count = (Number) query.getSingleResult();
		ret.put(Utils.DONUT_CHART_FINISHED_STUDENT_MISSIONS, count);
		
		jpql_sql = "SELECT count(t.id) FROM MissionStep t WHERE t.mission.instructor = :instructor";
		query = this.em.createQuery(jpql_sql);
		query.setParameter("instructor", instructor);
		count = (Number) query.getSingleResult();
		ret.put(Utils.DONUT_CHART_ALL_STEPS, count);
		
		jpql_sql = "SELECT count(s.id) FROM StudentMissionStep s WHERE s.complete IS NOT NULL AND s.missionStep.mission.instructor = :instructor";
		query = this.em.createQuery(jpql_sql);
		query.setParameter("instructor", instructor);
		count = (Number) query.getSingleResult();
		ret.put(Utils.DONUT_CHART_VISITED_MISSION_STEPS, count);
		
		return ret;
	}
}
