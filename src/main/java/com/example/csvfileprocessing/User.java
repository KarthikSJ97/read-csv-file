package com.example.csvfileprocessing;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "mobileNumber cannot be blank")
    private String mobileNumber;

    @NotBlank(message = "message cannot be blank")
    private String message;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @NotBlank(message = "firstName cannot be blank")
    private String firstName;

    @NotBlank(message = "points cannot be blank")
    private String points;

    @NotBlank(message = "reason cannot be blank")
    private String reason;

    public enum Channel{
        SMS, PUSH_NOTIFICATION
    }

}
