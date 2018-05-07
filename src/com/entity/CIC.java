package com.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class CIC {

	@Id
	@Column(name = "cicid")
	String cicid;
	@Column(name = "cictype")
	String cictype;
	@Column(name = "subject")
	String subject;
	@Column(name = "body")
	String body;
	@Column(name = "sourcesystem")
	String sourcesystem;
	@Column(name = "cicTimeStamp")
	Date cicTimeStamp;

	public CIC() {

	}

	public String getcic() {
		return cicid;
	}

	public void setcic(String cicid) {
		this.cicid = cicid;
	}

	public String getcictype() {
		return cictype;
	}

	public void setcictype(String cictype) {
		this.cictype = cictype;
	}

	public String getsubject() {
		return subject;
	}

	public void setsubject(String subject) {
		this.subject = subject;
	}

	public Date getcicTimeStamp() {
		return cicTimeStamp;
	}

	public void setcicTimeStamp(Date cicTimeStamp) {
		this.cicTimeStamp = cicTimeStamp;
	}

	public String getBody() {
		return body;
	}

	public void setbody(String body) {
		this.body = body;
	}

	public String getSourcesystem() {
		return sourcesystem;
	}

	public void setSourcesystem(String sourcesystem) {
		this.sourcesystem = sourcesystem;
	}

}