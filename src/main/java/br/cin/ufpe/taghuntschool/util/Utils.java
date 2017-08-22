
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

package br.cin.ufpe.taghuntschool.util;

public class Utils {
	public static final String COOKIE_KEY = "517888883642099305taghuntschool";
	public static final String PREFIX_URL = "https://ev.ifba.edu.br/taghuntschool/takeTip.xhtml?action=1&key=";
    public static final String FIND_INSTRUCTOR = "/pages/protected/Instructor/searchInstructor";
    public static final String EDIT_INSTRUCTOR = "/pages/protected/Instructor/createUpdateInstructor.xhtml";
    public static final String FIND_MISSION = "/pages/protected/Mission/searchMissionByInstructor.xhtml";
    public static final String EDIT_MISSION = "/pages/protected/Mission/createUpdateMission.xhtml";
    public static final String DETAIL_MISSION = "/pages/protected/Mission/viewDetailMission.xhtml";
    public static final String EDIT_MISSION_STEPS = "/pages/protected/Mission/createUpdateMissionSteps.xhtml";
    public static final String MANAGE_MISSION_STEPS = "/pages/protected/Mission/manageMissionSteps.xhtml";
    public static final String FIND_STUDENT_MISSION_BY_MISSION = "/pages/protected/StudentMission/studentMissionByMission.xhtml";
    public static final String DETAIL_STUDENT_MISSION_BY_MISSION = "/pages/protected/StudentMission/viewDetailStudentMission.xhtml";
	public static final String HOME_PROTECTED = "/pages/protected/homeAfterLogin";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final Integer ROLE_ADMIN_ID = 1;
    public static final Integer ROLE_INSTRUCTOR_ID = 2;
    public static final Integer ROLE_STUDENT_ID = 3;
    public static final String DONUT_CHART_ALL_MISSIONS = "AllMissions";
    public static final String DONUT_CHART_PUBLISHED_MISSIONS = "PublishedMissions";
    public static final String DONUT_CHART_ALL_STUDENT_MISSIONS = "AllStudentMissions";
    public static final String DONUT_CHART_FINISHED_STUDENT_MISSIONS = "FinishedStudentMissions";
    public static final String DONUT_CHART_ALL_STEPS = "AllSteps";
    public static final String DONUT_CHART_VISITED_MISSION_STEPS = "VisitedMissionSteps";

    public static String capitalizeStrings(String str) {

        int count = 0;
        StringBuilder token = new StringBuilder();
        String[] palavras = str.split(" ");
        String[] exc = new String[]{"e", "a", "o", "de", "da", "do", "das", "dos", "des",
            "em", "no", "na", "nos", "nas", "que", "com", "um", "uma",
            "uns", "se", "mas", "para", "delos"};

        for (String string : palavras) {
            for (String s : exc) {
                if (s.equalsIgnoreCase(string)) {
                    token.append(string.toLowerCase() + " ");
                    count++;
                }
            }
            if (count == 0) {
                token.append(string.substring(0, 1).toUpperCase());
                token.append(string.substring(1).toLowerCase());
                token.append(" ");
            }
            count = 0;
        }

        str = token.substring(0, token.length() - 1);
        str = token.toString().trim();

        return str;
    }

    public static String formataNome(String nome) {
        return nome.toLowerCase()
                .replace(" ", "-")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ú", "u")
                .replace("ç", "c")
                .replace("ó", "o")
                .replace("ã", "a")
                .replace("õ", "o")
                .replace("ê", "e")
                .replace("_", "");
    }

    public static String formataCPF(String cpf) {
        return cpf.substring(0, 3).concat(".").concat(cpf.substring(3, 6)).concat(".").concat(cpf.substring(6, 9)).concat("-").concat(cpf.substring(9, 11));
    }
}
