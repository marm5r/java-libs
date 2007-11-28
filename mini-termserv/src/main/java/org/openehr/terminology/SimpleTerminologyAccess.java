/*
 * component:   "openEHR Reference Implementation"
 * description: "Class SimpleTerminologyAccess"
 * keywords:    "terminology"
 *
 * author:      "Rong Chen <rong.acode@gmail.com>"
 * copyright:   "Copyright (c) 2007 Rong Chen"
 * license:     "See notice at bottom of class"
 *
 * file:        "$URL$"
 * revision:    "$LastChangedRevision$"
 * last_change: "$LastChangedDate$"
 */
package org.openehr.terminology;

import java.util.*;

import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.TerminologyAccess;

/**
 * Simple in-memory implementation of a terminology access
 * 
 * @author Rong Chen
 */
public class SimpleTerminologyAccess implements TerminologyAccess {
	
	/**
	 * Creates an simple terminology access
	 * 
	 * @param id
	 */
	SimpleTerminologyAccess(String id) {
		this.id = id;
		this.groups = new HashMap<String, Set<CodePhrase>>();
		this.groupLangNameToId = new HashMap<String, Map<String, String>>();
		this.codeRubrics = new HashMap<String, Map<String,String>>();
	}
	
	static SimpleTerminologyAccess getInstance(String id) {
		return new SimpleTerminologyAccess(id);
	}
	
	/**
	 * Adds a group of codes with language dependent names
	 * 
	 * @param groupId	not null  
	 * @param codes     not null
	 * @param names   <lang, name>  not null
	 */
	void addGroup(String groupId, Collection<String> codes, 
			Map<String, String> names) {
		
		Set<CodePhrase> group = new HashSet<CodePhrase>();
		for(String c : codes) {
			CodePhrase code = new CodePhrase(id, c);
			group.add(code);
		}
		groups.put(groupId, group);
		for(String lang : names.keySet()) {
			Map<String, String> nameToId = groupLangNameToId.get(lang);
			if(nameToId == null) {
				nameToId = new HashMap<String, String>();
			}
			String name = names.get(lang);
			nameToId.put(name, groupId);
			groupLangNameToId.put(lang, nameToId);
		}	
	}	
	
	/**
	 * Adds a rubric for given language and code
	 * 
	 * @param lang
	 * @param code
	 * @param rubric
	 */
	void addRubric(String lang, String code, String rubric) {
		Map<String, String> map = codeRubrics.get(lang);
		if(map == null) {
			map = new HashMap<String, String>();
			codeRubrics.put(lang, map);
		}
		map.put(code, rubric);
	}
	
	/**
	 * @returns "openehr"
	 */
	public String id() {
		return id;
	}

	public Set<CodePhrase> allCodes() {
		Set<CodePhrase> allCodes = new HashSet<CodePhrase>();
		for(Set<CodePhrase> codes : groups.values()) {
			allCodes.addAll(codes);
		}
		return allCodes;
	}

	public Set<CodePhrase> codesForGroupId(String groupID) {
		return groups.get(groupID); 
	}

	public Set<CodePhrase> codesForGroupName(String name, String language) {
		Map<String, String> map = groupLangNameToId.get(language);
		if(map == null) {
			return null;
		}
		String groupId = map.get(name);
		return groups.get(groupId);
	}

	public String rubricForCode(String code, String language) {
		Map<String, String> map = codeRubrics.get(language);
		if(map == null) {
			return null;			
		}
		return map.get(code);
	}
	
	public boolean hasCodeForGroupId(String groupId, CodePhrase code) {
		Set<CodePhrase> group = groups.get(groupId);
		if(group == null) {
			return false;
		}
		return group.contains(code);
	}

	/*
	 * Id of this terminology
	 */	
	private final String id;
	
	/*
	 * Groups indexed by group id
	 * <groupId, group of codes>
	 */
	private final Map<String, Set<CodePhrase>> groups;
	
	/**
	 * GroupIds indexed by language and group name
	 * <language, <groupName, groupId>>
	 */
	private final Map<String, Map<String, String>> groupLangNameToId; 
	
	/**
	 * Code rubrics indexed by lang, code
	 */
	private final Map<String, Map<String, String>> codeRubrics;	
 }
/*
 *  ***** BEGIN LICENSE BLOCK *****
 *  Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *  The contents of this file are subject to the Mozilla Public License Version
 *  1.1 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  The Original Code is SimpleTerminologyAccess.java
 *
 *  The Initial Developer of the Original Code is Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2007
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s):
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *  ***** END LICENSE BLOCK *****
 */