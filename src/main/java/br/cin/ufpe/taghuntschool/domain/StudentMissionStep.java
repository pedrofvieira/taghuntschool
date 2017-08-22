
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

package br.cin.ufpe.taghuntschool.domain;

import java.util.Date;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="general_usr_mission_steps")
@NamedQueries({
    @NamedQuery(name = "StudentMissionStep.findAll", query = "SELECT s FROM StudentMissionStep s")
})
public class StudentMissionStep implements Serializable {

	private static final long serialVersionUID = 7846733369498482072L;

	@Id
	@SequenceGenerator(name="STUDENT_MISSION_STEPS_ID_GENERATOR", sequenceName="general_usr_mission_steps_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUDENT_MISSION_STEPS_ID_GENERATOR")
	@Column(name="id")
	private Integer id;

	@JoinColumn(name = "mission_steps_fk_id", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private MissionStep missionStep;
	
	@JoinColumn(name = "general_usr_mission_id_fk", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private StudentMission studentMission;
	
	@Column(name="general_usr_mission_steps_ord")
	private Integer ord; 
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="general_usr_mission_steps_complete")
	private Date complete;
	
	public StudentMissionStep(){
		
	}

	public StudentMissionStep(MissionStep missionStep, Integer ord){
		this.missionStep = missionStep;
		this.ord = ord;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MissionStep getMissionStep() {
		return missionStep;
	}

	public void setMissionStep(MissionStep missionStep) {
		this.missionStep = missionStep;
	}

	public StudentMission getStudentMission() {
		return studentMission;
	}

	public void setStudentMission(StudentMission studentMission) {
		this.studentMission = studentMission;
	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public Date getComplete() {
		return complete;
	}

	public void setComplete(Date complete) {
		this.complete = complete;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentMissionStep other = (StudentMissionStep) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
