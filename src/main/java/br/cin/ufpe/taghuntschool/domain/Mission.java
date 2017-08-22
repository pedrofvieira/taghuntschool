
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

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="mission")
@NamedQueries({
    @NamedQuery(name = "Mission.findAll", query = "SELECT m FROM Mission m"),
    @NamedQuery(name = "Mission.findByTitle", query = "SELECT m FROM Mission m WHERE m.title = :title"),
    @NamedQuery(name = "Mission.findAllByInstructor", query = "SELECT m FROM Mission m WHERE m.instructor = :instructor"),
    @NamedQuery(name = "Mission.findByThingKey", query = "SELECT m FROM Mission m JOIN m.steps s WHERE s.thingKey = :thingKey"),
    @NamedQuery(name = "Mission.findPublishedByThingKey", query = "SELECT m FROM Mission m JOIN m.steps s WHERE m.published = true AND s.thingKey = :thingKey"),
    @NamedQuery(name = "Mission.findAllPublished", query = "SELECT m FROM Mission m WHERE m.published = true"),
    @NamedQuery(name = "Mission.countAll", query = "SELECT count(m.id) FROM Mission m"),
    @NamedQuery(name = "Mission.countAllByInstructor", query = "SELECT count(m.id) FROM Mission m WHERE m.instructor = :instructor"),
    @NamedQuery(name = "Mission.countAllPublished", query = "SELECT count(m.id) FROM Mission m WHERE m.published = true"),
    @NamedQuery(name = "Mission.countAllPublishedByInstructor", query = "SELECT count(m.id) FROM Mission m WHERE m.published = true AND m.instructor = :instructor")
})
public class Mission implements Serializable {

	private static final long serialVersionUID = 1236821966339468488L;

	public static final String FIND_BY_TITLE = "Mission.findByTitle";
	public static final String FIND_ALL_BY_INSTRUCTOR = "Mission.findAllByInstructor";
	public static final String FIND_BY_THING_KEY = "Mission.findByThingKey";
	public static final String FIND_PUBLISHED_BY_THING_KEY = "Mission.findPublishedByThingKey";
	public static final String FIND_ALL_PUBLISHED = "Mission.findAllPublished";
	public static final String COUNT_ALL = "Mission.countAll";
	public static final String COUNT_ALL_BY_INSTRUCTOR = "Mission.countAllByInstructor";
	public static final String COUNT_ALL_PUBLISHED = "Mission.countAllPublished";
	public static final String COUNT_ALL_PUBLISHED_BY_INSTRUCTOR = "Mission.countAllPublishedByInstructor";

	@Id
	@SequenceGenerator(name="MISSION_ID_GENERATOR", sequenceName="mission_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MISSION_ID_GENERATOR")
	@Column(name="id")
	private Integer id;

	@Size(max=200, message="O tamanho máximo para o campo Título é 200 caracteres!")
	@Column(name = "mission_title")
	private String title; 
	
	@Size(max=2000, message="O tamanho máximo para o campo Descrição é 2.000 caracteres!")
	@Column(name = "mission_desc")
	private String description; 
	
	@Size(max=80, message="O tamanho máximo para o campo Localização é 80 caracteres!")
	@Column(name = "mission_local")
	private String local; 
	
	@Column(name = "mission_published")
	private boolean published;

    @JoinColumn(name = "general_usr_id_fk", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
	private GeneralUsr instructor;

    @OneToMany(mappedBy="mission", orphanRemoval=true, cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MissionStep> steps;
    
    public Mission(){
    	this.published =  false;
    }
    
    public Mission(GeneralUsr instructor_owner){
    	this.instructor = instructor_owner;
    	this.published =  false;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public GeneralUsr getInstructor() {
		return instructor;
	}

	public void setInstructor(GeneralUsr instructor) {
		this.instructor = instructor;
	}

	public List<MissionStep> getSteps() {
		return steps;
	}

	public void setSteps(List<MissionStep> steps) {
		this.steps = steps;
	}

	public MissionStep getMissionStep(String thingKey){
		if(getSteps() != null){
			for(MissionStep item : getSteps()){
				if(item.getThingKey().equals(thingKey)){
					return item;
				}
			}
		}
		return null;
	}

	public void addMissionStep(MissionStep missionStep){
		MissionStep item = getMissionStep(missionStep.getThingKey());
		if(item == null){
			missionStep.setMission(this);
			getSteps().add(missionStep);
		}
	}
	
	public void removeMissionStep(MissionStep missionStep){
		getSteps().remove(missionStep);
		missionStep.setMission(null);
	}
	
	public void updateMissionStep(MissionStep missionStep){
		MissionStep item = getMissionStep(missionStep.getThingKey());
		if(item != null){
			item.setThingName(missionStep.getThingName());;
			item.setTip(missionStep.getTip());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
		Mission other = (Mission) obj;
		if (this.title == null) {
			if (other.title != null)
				return false;
		} else if (!this.title.equals(other.title))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}
}
