
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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.util.UUID;
import java.nio.ByteBuffer;

import org.apache.commons.codec.binary.Base64;

public class SecurityUtil {
	
	public static String criptografa(String senha) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
	}

	public static String base64Encode(String stringToEncode) {
		byte[] stringToEncodeBytes = stringToEncode.getBytes();
		return Base64.encodeBase64String(stringToEncodeBytes);
	}

	public static String base64Decode(String stringToDecode) {
		byte[] decodedBytes = Base64.decodeBase64(stringToDecode);
		return new String(decodedBytes);
	}
	
	public static String geraToken500URL() {
		SecureRandom prng = new SecureRandom();

	    String aux = new Integer(prng.nextInt()).toString();
	    byte[] stringToEncodeBytes = aux.getBytes();
	    aux = Base64.encodeBase64URLSafeString(stringToEncodeBytes);
		return (aux.length() > 500 ? aux.substring(1, 500) : aux);
	}

	public static String geraToken60URL() {
		SecureRandom prng = new SecureRandom();

	    String aux = new Integer(prng.nextInt()).toString();
	    byte[] stringToEncodeBytes = aux.getBytes();
	    aux = Base64.encodeBase64URLSafeString(stringToEncodeBytes);
		return (aux.length() > 60 ? aux.substring(1, 60) : aux);
	}
	
    public static String newUuid() { 
        UUID uuid = UUID.randomUUID(); 
        return uuid.toString(); 
    } 
 
    public static String newUuidBase64() { 
        String uuidStr = newUuid(); 
        return uuidToBase64(uuidStr); 
    } 
 
    public static String uuidToBase64(String str) { 
        UUID uuid = UUID.fromString(str); 
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]); 
        bb.putLong(uuid.getMostSignificantBits()); 
        bb.putLong(uuid.getLeastSignificantBits()); 
        return Base64.encodeBase64URLSafeString(bb.array()); 
    } 
 
    public static String uuidFromBase64(String str) { 
        byte[] bytes = Base64.decodeBase64(str); 
        ByteBuffer bb = ByteBuffer.wrap(bytes); 
        UUID uuid = new UUID(bb.getLong(), bb.getLong()); 
        return uuid.toString(); 
    }
}
