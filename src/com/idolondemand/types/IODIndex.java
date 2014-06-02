package com.idolondemand.types;

public class IODIndex {
	
	private String index;
	private String flavor;
	private String type;
	private String date_created;
	private Integer num_components;
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getFlavor() {
		return flavor;
	}
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDate_created() {
		return date_created;
	}
	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}
	public Integer getNum_components() {
		return num_components;
	}
	public void setNum_components(Integer num_components) {
		this.num_components = num_components;
	}
	
	private String crlf = "\r\n";
	public String toString(){
		
		StringBuffer sb = new StringBuffer();
		sb.append("index: ").append(index).append(",").append(crlf)
		.append("flavor: ").append(flavor).append(",").append(crlf)
		.append("type: ").append(type).append(",").append(crlf)
		.append("date_created: ").append(date_created).append(",").append(crlf)
		.append("num_components: ").append(num_components).append(",").append(crlf);
		return sb.toString();
		
	}
	
}
