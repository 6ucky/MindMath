package com.mocah.mindmath.server.entity.feedback;

public class ContentFBHTML {
	
	private String default_img_url;
	private String video_url;
	private String video_srt_url;
	
	public ContentFBHTML(String default_img_url, String video_url, String video_srt_url) {
		this.setDefault_img_url(default_img_url);
		this.setVideo_url(video_url);
		this.setVideo_srt_url(video_srt_url);
	}

	public String getDefault_img_url() {
		return default_img_url;
	}

	public void setDefault_img_url(String default_img_url) {
		this.default_img_url = default_img_url;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public String getVideo_srt_url() {
		return video_srt_url;
	}

	public void setVideo_srt_url(String video_srt_url) {
		this.video_srt_url = video_srt_url;
	}

}
