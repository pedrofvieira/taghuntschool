
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.security.Principal;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.service.GeneralUsrService;
import br.cin.ufpe.taghuntschool.service.MissionService;
import br.cin.ufpe.taghuntschool.util.Utils;
import br.cin.ufpe.taghuntschool.util.FacesContextUtil;

@ManagedBean
@SessionScoped
public class InicioMB implements Serializable {

	private static final long serialVersionUID = 2720044224651934862L;

	private GeneralUsr usuario;
	
	private MenuModel menuModel;
	
	private DonutChartModel donutChart1;
	
	private DonutChartModel donutChart2;
	
	private DonutChartModel donutChart3;
	
	private Boolean isUserAdmin;
	
	private Boolean isUserInstructor;

	@Inject
	private GeneralUsrService usuarioService;

    @Inject
	private MissionService missionService;
	
	public GeneralUsr getUsuario() {
		return usuario;
	}

	public void setUsuario(GeneralUsr usuario) {
		this.usuario = usuario;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}

	public DonutChartModel getDonutChart1() {
		return donutChart1;
	}

	public void setDonutChart1(DonutChartModel donutChart1) {
		this.donutChart1 = donutChart1;
	}

	public DonutChartModel getDonutChart2() {
		return donutChart2;
	}

	public void setDonutChart2(DonutChartModel donutChart2) {
		this.donutChart2 = donutChart2;
	}

	public DonutChartModel getDonutChart3() {
		return donutChart3;
	}

	public void setDonutChart3(DonutChartModel donutChart3) {
		this.donutChart3 = donutChart3;
	}

	public Boolean getIsUserAdmin() {
		return isUserAdmin;
	}

	public void setIsUserAdmin(Boolean isUserAdmin) {
		this.isUserAdmin = isUserAdmin;
	}

	public Boolean getIsUserInstructor() {
		return isUserInstructor;
	}

	public void setIsUserInstructor(Boolean isUserInstructor) {
		this.isUserInstructor = isUserInstructor;
	}
	
	@PostConstruct
    public void init() {
		Map<String, Number> missionStats = null;

		this.isUserAdmin = false;
		this.isUserInstructor = false;

		try {
			if(this.usuario == null){
				this.usuario = (GeneralUsr) FacesContextUtil.getAttributeHttpSession("usuarioLogado");
			}
			if(this.usuario ==  null || this.usuario.getId() == null){
			
				Principal principal = FacesContextUtil.getFacesContextCurrent().getExternalContext().getUserPrincipal();		
				if(principal == null){
					// Usuário não logado
				} else {
					this.usuario = usuarioService.loadUserByUsername(principal.getName());
					FacesContextUtil.setAttributeHttpSession("usuarioLogado", this.usuario);
				}
			}
			if(this.usuario == null){
				// Usuário inexistente
			} else {
				FacesContextUtil.setMensagemNormal("Bem-Vindo " + this.usuario.getName() + "!");
			}

			if(FacesContextUtil.isUserInRole("ROLE_ADMIN")){
				missionStats = missionService.getMissionStats();
			} else {
				missionStats = missionService.getMissionStats(this.usuario);
			}
		
		} catch (Exception e) {
			FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
	        e.printStackTrace();
		}
		
		DefaultMenuItem item;
		
        this.menuModel = new DefaultMenuModel();
        
        if(FacesContextUtil.isUserInRole("ROLE_ADMIN")){
			setIsUserAdmin(true);
			setIsUserInstructor(false);
       	
	        DefaultSubMenu firstSubmenu = new DefaultSubMenu("Administrativo");
	        
	        item = new DefaultMenuItem("Instrutores");
	        item.setCommand("#{generalUsrMB.prepareSearchInstructor()}");
	        item.setAjax(false);
	        item.setIcon("fa fa-fw fa-user");
	        firstSubmenu.addElement(item);
	 
	        menuModel.addElement(firstSubmenu);
        }
        
        if(FacesContextUtil.isUserInRole("ROLE_INSTRUCTOR")){
			setIsUserAdmin(false);
			setIsUserInstructor(true);
           	
	        DefaultSubMenu fourthSubmenu = new DefaultSubMenu("Instrutor");
	        
	        item = new DefaultMenuItem("Missões");
	        item.setCommand("#{missionMB.prepareSearch()}");
	        item.setAjax(false);
	        item.setIcon("fa fa-fw fa-tags");
	        fourthSubmenu.addElement(item);
	 
	        menuModel.addElement(fourthSubmenu);
        }
        DefaultSubMenu fifthSubmenu = new DefaultSubMenu("Geral");
        
        item = new DefaultMenuItem("Página Inicial");
        item.setCommand("#{inicioMB.prepararPaginaInicial()}");
        item.setAjax(false);
        item.setIcon("fa fa-fw fa-home");
        fifthSubmenu.addElement(item);
       
        item = new DefaultMenuItem("Sair");
        item.setIcon("fa fa-fw fa-power-off");
        item.setUrl(Utils.LOGOUT);
        fifthSubmenu.addElement(item);
 
        menuModel.addElement(fifthSubmenu);
        
        initDonutCharts(missionStats);        
	}

	public String prepararPaginaInicial(){
		Map<String, Number> missionStats = null;
		try
		{
			if(FacesContextUtil.isUserInRole("ROLE_ADMIN")){
				missionStats = missionService.getMissionStats();
			} else {
				missionStats = missionService.getMissionStats(this.usuario);
			}
		
		} catch (Exception e) {
			FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
	        e.printStackTrace();
		}
		initDonutCharts(missionStats);
		return Utils.HOME_PROTECTED;
	}

    private void initDonutCharts(Map<String, Number> missionStats) {
    	Long countAux1, countAux2;
        Map<String, Number> circleAux = new LinkedHashMap<String, Number>();

        countAux1 = countAux2 = 0L;
        if(missionStats.containsKey(Utils.DONUT_CHART_ALL_MISSIONS)) {
     	   countAux1 = (Long) missionStats.get(Utils.DONUT_CHART_ALL_MISSIONS);
        }
        if(missionStats.containsKey(Utils.DONUT_CHART_PUBLISHED_MISSIONS)) {
        	countAux2 = (Long) missionStats.get(Utils.DONUT_CHART_PUBLISHED_MISSIONS);
         }
        if(!countAux1.equals(0L) || !countAux2.equals(0L)){
        	this.donutChart1 = new DonutChartModel();
            circleAux.put("Missões Cadastradas", countAux1);
            circleAux.put("Missões Publicadas", countAux2);
            this.donutChart1.addCircle(circleAux);
            this.donutChart1.setTitle("Missões");
            this.donutChart1.setLegendPosition("e");
            this.donutChart1.setSliceMargin(5);
            this.donutChart1.setShowDataLabels(true);
            this.donutChart1.setDataFormat("value");
            this.donutChart1.setExtender("donutExtender");
            this.donutChart1.setSeriesColors("04C1FB, FBFB18");
        } else {
        	this.donutChart1 = null;
        }
        
        countAux1 = countAux2 = 0L;
        if(missionStats.containsKey(Utils.DONUT_CHART_ALL_STUDENT_MISSIONS)) {
        	countAux1 = (Long) missionStats.get(Utils.DONUT_CHART_ALL_STUDENT_MISSIONS);
        }
        if(missionStats.containsKey(Utils.DONUT_CHART_FINISHED_STUDENT_MISSIONS)) {
        	countAux2 = (Long) missionStats.get(Utils.DONUT_CHART_FINISHED_STUDENT_MISSIONS);
        }
        if(!countAux1.equals(0L) || !countAux2.equals(0L)){
            this.donutChart2 = new DonutChartModel();
            circleAux = new LinkedHashMap<String, Number>();
	        circleAux.put("Iniciadas", countAux1);
	        circleAux.put("Concluídas", countAux2);
	        this.donutChart2.addCircle(circleAux);
	        this.donutChart2.setTitle("Missões de Estudantes");
	        this.donutChart2.setLegendPosition("w");
	        this.donutChart2.setSliceMargin(5);
	        this.donutChart2.setShowDataLabels(true);
	        this.donutChart2.setDataFormat("value");
	        this.donutChart2.setExtender("donutExtender");
        } else {
        	this.donutChart2 = null;
        }
        
        countAux1 = countAux2 = 0L;
        if(missionStats.containsKey(Utils.DONUT_CHART_ALL_STEPS)) {
        	countAux1 = (Long) missionStats.get(Utils.DONUT_CHART_ALL_STEPS);
        }
        if(missionStats.containsKey(Utils.DONUT_CHART_VISITED_MISSION_STEPS)) {
        	countAux2 = (Long) missionStats.get(Utils.DONUT_CHART_VISITED_MISSION_STEPS);
        }
        if(!countAux1.equals(0L) || !countAux2.equals(0L)){
            this.donutChart3 = new DonutChartModel();
            circleAux = new LinkedHashMap<String, Number>();
	        circleAux.put("Não visitados", countAux1);
	        circleAux.put("Visitados", countAux2);
	        this.donutChart3.addCircle(circleAux);
	        this.donutChart3.setTitle("Objetos de Missões");
	        this.donutChart3.setLegendPosition("e");
	        this.donutChart3.setSliceMargin(5);
	        this.donutChart3.setShowDataLabels(true);
	        this.donutChart3.setDataFormat("value");
	        this.donutChart3.setExtender("donutExtender");
	        this.donutChart3.setSeriesColors("AA8C5C, B7B712");
        } else {
        	this.donutChart3 = null;
        }
        
    }
}
