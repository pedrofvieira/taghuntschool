
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

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

import br.cin.ufpe.taghuntschool.comparator.StudentMissionStepComparator;

@Entity
@Table(name="general_usr_mission")
@NamedQueries({
    @NamedQuery(name = "StudentMission.findAll", query = "SELECT s FROM StudentMission s"),
    @NamedQuery(name = "StudentMission.findByMission", query = "SELECT s FROM StudentMission s WHERE s.mission = :mission"),
    @NamedQuery(name = "StudentMission.findByStudentAndMission", query = "SELECT s FROM StudentMission s WHERE s.mission = :mission AND s.student = :student")
})
public class StudentMission implements Serializable {

	private static final long serialVersionUID = -8018244674701601839L;

	public static final String FIND_BY_MISSION = "StudentMission.findByMission";
	public static final String FIND_BY_STUDENT_AND_MISSION = "StudentMission.findByStudentAndMission";
	
	@Id
	@SequenceGenerator(name="STUDENT_MISSION_ID_GENERATOR", sequenceName="general_usr_mission_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="STUDENT_MISSION_ID_GENERATOR")
	@Column(name="id")
	private Integer id;

	@JoinColumn(name = "mission_id_fk", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Mission mission;

	@JoinColumn(name = "general_usr_id_fk", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private GeneralUsr student;

	@Column(name = "mission_finished")
	private boolean finished;

    @OneToMany(mappedBy="studentMission", orphanRemoval=true, cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StudentMissionStep> studentMissionSteps;

    public StudentMission(){
		
	}
	
	public StudentMission(Mission mission, GeneralUsr student){
		this.mission = mission;
		this.student = student;
		this.finished = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
		this.mission = mission;
	}

	public GeneralUsr getStudent() {
		return student;
	}

	public void setStudent(GeneralUsr student) {
		this.student = student;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public List<StudentMissionStep> getStudentMissionSteps() {
		return studentMissionSteps;
	}

	public void setStudentMissionSteps(List<StudentMissionStep> studentMissionSteps) {
		this.studentMissionSteps = studentMissionSteps;
	}

	public Boolean addStudentMissionStep(StudentMissionStep studentMissionStep){
		StudentMissionStep studentMissionStepExists = getStudentMissionStep(studentMissionStep.getMissionStep());
		if(studentMissionStepExists == null){
			studentMissionStep.setStudentMission(this);
			getStudentMissionSteps().add(studentMissionStep);
			return true;
		}
		return false;
	}
	
	public Boolean completeStudentMissionStep(StudentMissionStep studentMissionStep){
		StudentMissionStep studentMissionStepExists = getStudentMissionStep(studentMissionStep.getMissionStep());
		if(studentMissionStepExists != null){
			studentMissionStepExists.setComplete(new Date());
			return true;
		}
		return false;
	}
	
	public void removeStudentMissionStep(StudentMissionStep studentMissionStep){
		getStudentMissionSteps().remove(studentMissionStep);
		studentMissionStep.setStudentMission(null);
	}

	public StudentMissionStep getStudentMissionStep(MissionStep missionStep){
		if(getStudentMissionSteps() != null){
			for(StudentMissionStep item : getStudentMissionSteps()){
				if(item.getMissionStep().equals(missionStep)){
					return item;
				}
			}
		}
		return null;
	}
	
	public void generateOrderedStudentMissionSteps(MissionStep missionStepStart){
		Integer ordSteps = 0;
		
		List<MissionStep> steps = getMission().getSteps();
		if(steps != null){
			StudentMissionStep newStep = new StudentMissionStep( missionStepStart, ordSteps); 
			newStep.setComplete(new Date());
			addStudentMissionStep(newStep);
			ordSteps++;

			long seed = System.nanoTime();
			Collections.shuffle(steps, new Random(seed));
			for(MissionStep item : steps){
				if (addStudentMissionStep(new StudentMissionStep( item, ordSteps))){
					ordSteps++;
				}
			}
		}
	}
	
	public StudentMissionStep findNextStudentMissionStep(){
		List<StudentMissionStep> auxSteps = getStudentMissionSteps();
		
		if(auxSteps != null){
			Collections.sort(auxSteps, new StudentMissionStepComparator());
			for(StudentMissionStep item : auxSteps){
				if(item.getComplete() == null){
					return item;
				}
			}
		}
		return null;
	}
	
//	public StudentMissionStep findNextByCurrentOrd(Integer currentOrd){
//		Integer nextOrd = currentOrd + 1;
//		if(getStudentMissionSteps() != null){
//			if(getStudentMissionSteps().size() > nextOrd ){
//				for(StudentMissionStep item : getStudentMissionSteps()){
//					if(item.getOrd().equals(nextOrd)){
//						return item;
//					}
//				}
//			}
//		}
//		return null;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		StudentMission other = (StudentMission) obj;
		if (this.id == null) {
			if (other.id != null)
				return false;
		} else if (!this.id.equals(other.id))
			return false;
		return true;
	}

}
