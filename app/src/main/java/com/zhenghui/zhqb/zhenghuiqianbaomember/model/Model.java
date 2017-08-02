package com.zhenghui.zhqb.zhenghuiqianbaomember.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Model implements Parcelable {

	private String index;
	private String name;
	private String title;
	private String image;
	private String type;
	private String locationCity;
	private String longitude;
	private String latitude;

	public Model(String index, String name) {
		this.index = index;
		this.name = name;
	}

	// 重写describeContents方法，内容接口描述，默认返回0就可以，基本不用
	@Override
	public int describeContents() {
		return 0;
	}

	// 重写writeToParcel方法，将你的对象序列化为一个Parcel对象，即：将类的数据写入外部提供的Parcel中，打包需要传递的数据到Parcel容器保存，以便从
	// Parcel容器获取数据
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(index);
		dest.writeString(name);
	}

	/**
	 * 其中public static
	 * final一个都不能少，内部对象CREATOR的名称也不能改变，必须全部大写。需重写本接口中的两个方法：createFromParcel
	 * (Parcel in) 实现从Parcel容器中读取传递数据值，封装成Parcelable对象返回逻辑层，newArray(int size)
	 * 创建一个类型为T，长度为size的数组，仅一句话即可（return new T[size]），供外部类反序列化本类数组使用。
	 */
	public static final Creator<Model> CREATOR = new Creator<Model>() {

		@Override
		public Model createFromParcel(Parcel source) {
			return new Model(source.readString(), source.readString());
		}

		@Override
		public Model[] newArray(int size) {
			return new Model[size];
		}

	};

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
