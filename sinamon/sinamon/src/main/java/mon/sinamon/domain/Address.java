package mon.sinamon.domain;

import lombok.Getter;

import javax.persistence.Embeddable;


@Embeddable
@Getter
public class Address {

    private String address;
    private String logitude;    //경도(x축)
    private String latitude;    //위도(y축)
    private String zipcode; //우편번호
    private String more_address; //추가 주소
    private String last_address; //참고사항

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



    public Address(String address, String logitude, String latitude,String zipcode, String more_address, String last_address){
        this.address=address;
        this.logitude=logitude;
        this.latitude=latitude;
        this.zipcode=zipcode;
        this.more_address=more_address;
        this.last_address=last_address;
    }
}
