package br.cin.ufpe.taghuntschool.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import br.cin.ufpe.taghuntschool.domain.Mission;
import br.cin.ufpe.taghuntschool.domain.StudentMission;
import br.cin.ufpe.taghuntschool.service.StudentMissionService;
import br.cin.ufpe.taghuntschool.util.FacesContextUtil;
import br.cin.ufpe.taghuntschool.util.Utils;

@ManagedBean
@ViewScoped
public class StudentMissionMB implements Serializable {

	private static final long serialVersionUID = 4812494425116891629L;

    private Mission missionEdited;
    
	private List<StudentMission> studentMissionList; 
	
    private StudentMission studentMissionEdited;
    
    @Inject
	private StudentMissionService studentMissionService;
    
	@PostConstruct
    public void init() {
		try {
			
			if(this.getMissionEdited() == null){
				this.setMissionEdited( (Mission) FacesContextUtil.getAttributeHttpSession("missionEdited"));
			}
			this.setStudentMissionList( new ArrayList<StudentMission>() );
			if(this.getMissionEdited() != null){
				this.setStudentMissionList( studentMissionService.findByMission(this.getMissionEdited()) );
			}
			if(this.getStudentMissionEdited() == null){
				this.setStudentMissionEdited( (StudentMission) FacesContextUtil.getAttributeHttpSession("studentMissionEdited"));
			}
		} catch (Exception e) {
	    	FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
	        e.printStackTrace();
		}
	}
	
	public String prepareStudentMissionByMission(){
		return Utils.FIND_STUDENT_MISSION_BY_MISSION;
	}
	
    public String prepareViewDetail(StudentMission studentMission){
    	this.setStudentMissionEdited(studentMission);
		FacesContextUtil.setAttributeHttpSession("studentMissionEdited", this.getStudentMissionEdited());
		
		return Utils.DETAIL_STUDENT_MISSION_BY_MISSION;
    }
   
	public String retMissionViewDetail(){
		FacesContextUtil.setAttributeHttpSession("missionEdited", this.getMissionEdited());
		
		return Utils.DETAIL_MISSION;
	}

	public Mission getMissionEdited() {
		return missionEdited;
	}

	public void setMissionEdited(Mission missionEdited) {
		this.missionEdited = missionEdited;
	}

	public List<StudentMission> getStudentMissionList() {
		return studentMissionList;
	}

	public void setStudentMissionList(List<StudentMission> studentMissionList) {
		this.studentMissionList = studentMissionList;
	}

	public StudentMission getStudentMissionEdited() {
		return studentMissionEdited;
	}

	public void setStudentMissionEdited(StudentMission studentMissionEdited) {
		this.studentMissionEdited = studentMissionEdited;
	}
}
