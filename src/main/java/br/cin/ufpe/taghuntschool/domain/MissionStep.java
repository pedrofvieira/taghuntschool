
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
import javax.validation.constraints.Size;

import br.cin.ufpe.taghuntschool.util.Utils;

@Entity
@Table(name="mission_steps")
@NamedQueries({
    @NamedQuery(name = "MissionStep.findAll", query = "SELECT s FROM MissionStep s"),
    @NamedQuery(name = "MissionStep.findByThingName", query = "SELECT s FROM MissionStep s WHERE s.thingName = :thingName"),
    @NamedQuery(name = "MissionStep.findByThingKey", query = "SELECT s FROM MissionStep s WHERE s.thingKey = :thingKey")
})
public class MissionStep implements Serializable {

	private static final long serialVersionUID = -9071856963024777358L;

	public static final String FIND_BY_THING_NAME = "MissionStep.findByThingName";
	public static final String FIND_BY_THING_KEY = "MissionStep.findByThingKey";

	@Id
	@SequenceGenerator(name="MISSION_STEPS_ID_GENERATOR", sequenceName="mission_steps_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MISSION_STEPS_ID_GENERATOR")
	@Column(name="id")
	private Integer id;

	@Size(max=2000, message="O tamanho máximo para o campo 'Dica para encontrar o objeto' é 2.000 caracteres!")
	@Column(name="mission_steps_tip")
	private String tip;
	
	@Size(max=500, message="O tamanho máximo para o campo Nome do Objeto é 500 caracteres!")
	@Column(name = "mission_steps_thing_name")
	private String thingName; 
	
	@Column(name = "mission_steps_thing_key")
	private String thingKey; 
	
	@JoinColumn(name = "mission_fk_id", referencedColumnName = "id")
	@ManyToOne(optional = false)
	private Mission mission;
	
	public MissionStep(){
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public String getThingName() {
		return thingName;
	}

	public void setThingName(String thingName) {
		this.thingName = thingName;
	}

	public String getThingKey() {
		return thingKey;
	}

	public void setThingKey(String thingKey) {
		this.thingKey = thingKey;
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public String getMissionStepURL() {
		String ret = "";
		if(this.thingKey != null){
			ret = Utils.PREFIX_URL + this.thingKey;
		}
		return ret;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.thingName == null) ? 0 : this.thingName.hashCode());
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
		MissionStep other = (MissionStep) obj;
		if (this.thingName == null) {
			if (other.thingName != null)
				return false;
		} else if (!this.thingName.equals(other.thingName))
			return false;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}	
}
