package com.example.csvfileprocessing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mobileNumber;
    private String message;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    private String firstName;
    private String points;
    private String reason;

    public enum Channel{
        SMS, PUSH_NOTIFICATION
    }

}
