
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
import java.io.StringWriter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import br.cin.ufpe.taghuntschool.domain.GeneralUsr;
import br.cin.ufpe.taghuntschool.service.GeneralUsrService;
import br.cin.ufpe.taghuntschool.util.FacesContextUtil;
import br.cin.ufpe.taghuntschool.util.SecurityUtil;
import br.cin.ufpe.taghuntschool.util.Utils;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

@ManagedBean
@ViewScoped
public class UtilGeneralUsrMB implements Serializable {

	private static final long serialVersionUID = 1041254399222573476L;

    private String action;

	private String key;

	private String emailRecuperarSenha;
	
    private GeneralUsr usuario;
	
    @Inject
	private GeneralUsrService generalUsrService;

	@Resource(lookup = "java:jboss/mail/Default")
	private Session mailSession;

    @PostConstruct
    public void init() {
    	
    }

    
	public String recuperarSenhaUsuario(){
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				this.setUsuario(generalUsrService.findByEmail(this.emailRecuperarSenha));
				
				if(this.getUsuario() ==  null || !this.getUsuario().isEnabled()){

					FacesContextUtil.setMensagemErro("Não foi encontrado um usuário ATIVO para o email fornecido.");

				} else {

					enviarEmailRecuperarSenha(this.getUsuario());
					
					FacesContextUtil.setMensagemNormal("A sua solicitação de recuperação de senha de usuário foi registrada com sucesso e um e-mail de recuperação foi encaminhado. Para finalizar o processo basta clicar no link existente no e-mail.");
					FacesContextUtil.setAttributeHttpSession("usuarioEmEdicao", null);
			    	FacesContextUtil.setAttributeHttpSession("instructorEdited", null);
					return Utils.LOGIN;
				}
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
		return null;
	}

	private void enviarEmailRecuperarSenha(GeneralUsr usuario) throws MessagingException {
		Message msg = new MimeMessage(mailSession);

		msg.setSubject("Recuperação de Senha - Plataforma Taghunt School ");
		msg.setRecipient(RecipientType.TO, new InternetAddress(usuario.getEmail()));

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve.init();
		VelocityContext context = new VelocityContext();
		context.put("usuario", usuario);
		context.put("loginHash", SecurityUtil.base64Encode(usuario.getUsername()));
		context.put("context", FacesContextUtil.getContextPath());
		
		Template t = ve.getTemplate("emailtemplates/emailrecuperacaosenha.vn");
		StringWriter writer = new StringWriter();
		t.merge(context, writer);

		BodyPart body = new MimeBodyPart();
		body.setContent(writer.toString(), "text/html");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(body);
		msg.setContent(multipart, "text/html");

		Transport.send(msg);

	}

	public void prepararRecuperarSenha(){
		
		if (this.getAction() == null || this.getKey() == null) {
			return;
		}

		try {
			String username = SecurityUtil.base64Decode(this.getKey());
	    	
			this.setUsuario(generalUsrService.loadUserByUsername(username));
			if(this.getUsuario() ==  null || !this.getUsuario().isEnabled()){

				FacesContextUtil.setMensagemErro("Não foi encontrado um usuário ATIVO para o email fornecido.");

			} else {
				this.getUsuario().setPassword(null);
			}
				
        } catch (Exception e) {
			FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
			
            e.printStackTrace();
		}
	}

    public String gravarSenhaUsuario(){
	    FacesContext context = FacesContextUtil.getFacesContextCurrent();
		   
	    if(!context.getRenderResponse()){
		
			try {
				generalUsrService.updatePassword(this.getUsuario());
				
				FacesContextUtil.setMensagemNormal("A atualização da senha de usuário foi realizada com sucesso!");
				
				FacesContextUtil.setAttributeHttpSession("usuarioEmEdicao", null);
		    	FacesContextUtil.setAttributeHttpSession("instructorEdited", null);

				return Utils.LOGIN;
	        } catch (Exception e) {
				FacesContextUtil.setMensagemFatal("Um erro inesperado aconteceu");
				
	            e.printStackTrace();
			}
	    }
    	return null;
    }
	
    public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getEmailRecuperarSenha() {
		return emailRecuperarSenha;
	}

	public void setEmailRecuperarSenha(String emailRecuperarSenha) {
		this.emailRecuperarSenha = emailRecuperarSenha;
	}

	public GeneralUsr getUsuario() {
		return usuario;
	}

	public void setUsuario(GeneralUsr usuario) {
		this.usuario = usuario;
	}

}
