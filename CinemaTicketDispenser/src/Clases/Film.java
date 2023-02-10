package Clases;




import java.io.File;
import java.io.Serializable;

public class Film implements Serializable {

    private String name;
    private File  poster;
    private int duration;
    private String description;
    
    public File img;
    
    
    public Film(String name,File poster, int duration,String description){
        this.name=name;
        this.poster=poster;
        this.duration=duration;
        this.description=description;
    }

    public Film() {
        
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getPoster() {
		return poster;
	}

	public void setPoster(File poster) {
		this.poster = poster;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getImg() {
		return img;
	}

	public void setImg(File img) {
		this.img = img;
	}
      
}
