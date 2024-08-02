//package com.example.HungerBox_Backend.Response;
//
//import com.example.HungerBox_Backend.Model.USER_ROLE;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class AuthResponse {
//
//    private String jwt;
//
//    private String message;
//
//    private USER_ROLE role;
//}
//
//






// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - -

package com.example.HungerBox_Backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String jwt;

    private String message;

    private int role; // Integer role (0 for Customer, 1 for Vendor)
}
