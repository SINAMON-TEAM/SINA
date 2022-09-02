package mon.sinamon.domain;

import lombok.Getter;

import javax.persistence.Embeddable;


@Embeddable
@Getter
public class Address {

    private String address;
    private String logitude;    //경도(x축)
    private String latitude;    //위도(y축)
    private String zipcode;

    protected Address(){
    }

    public Address(String address){
        this.address=address;
    }

    public Address(String longitude,String latitude) {
        this.logitude=longitude;
        this.latitude=latitude;
    }

    public Address(String address, String logitude, String latitude,String zipcode){
        this.address=address;
        this.logitude=logitude;
        this.latitude=latitude;
        this.zipcode=zipcode;
    }
}
