
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class FacesContextUtil {
	
	public static FacesContext getFacesContextCurrent(){
		return FacesContext.getCurrentInstance();
	}
	
	public static String getFacesCurrentViewID(){
		return getFacesContextCurrent().getViewRoot().getViewId();
	}
	
	public static HttpServletRequest getFacesCurrentHttpServletRequest(){
		return (HttpServletRequest) getFacesContextCurrent().getExternalContext().getRequest();
	}
	
	public static String getCookieValue(String key){
		HttpServletRequest req = getFacesCurrentHttpServletRequest();
		Cookie[] cookies = req.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals(key)) {
	                return cookie.getValue();
	            }
	        }
	    }
	    return null;
	}
	
	public static void setCookieValue(String key, String value){
		ExternalContext ec = getFacesContextCurrent().getExternalContext();
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("maxAge", 31536000); // 60*60*24*365 = 31536000
        ec.addResponseCookie(key, value, props);
	}
	
	public static String getContextPath() {
        HttpServletRequest req = (HttpServletRequest) getFacesContextCurrent().getExternalContext().getRequest();
        String url = req.getRequestURL().toString();
        return url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
    }

	public static boolean isUserInRole(String role){
		HttpServletRequest req = (HttpServletRequest) getFacesContextCurrent().getExternalContext().getRequest();
		return req.isUserInRole(role);
	}
	
	public static void setAttributeHttpSession(String atributo, Object obj){
		HttpSession session = (HttpSession) getFacesContextCurrent().getExternalContext().getSession(true);
		session.setAttribute(atributo, obj);
	}
	
	public static Object getAttributeHttpSession(String atributo){
		HttpServletRequest req = (HttpServletRequest) getFacesContextCurrent().getExternalContext().getRequest();
		return req.getSession().getAttribute(atributo);
	}
	
	public static void setMensagemErro(String mensagem) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, mensagem, mensagem);
        getFacesContextCurrent().addMessage(null, fm);
	}
	
	public static void setMensagemNormal(String mensagem) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, mensagem);
        getFacesContextCurrent().addMessage(null, fm);
	}
	
	public static void setMensagemFatal(String mensagem) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_FATAL, mensagem, mensagem);
        getFacesContextCurrent().addMessage(null, fm);
	}
	
	public static StreamedContent converteParaPDF(byte[] bytes, String fileName) {
        InputStream is = new ByteArrayInputStream(bytes);
        StreamedContent file = new DefaultStreamedContent(is, "application/pdf", fileName);
        return file;
    }
	
	public static Long wordsCounter(String texto){
		StringTokenizer parser = new StringTokenizer(texto);
		return (long) parser.countTokens();
	}
	


}
