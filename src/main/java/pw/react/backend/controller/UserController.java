package pw.react.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.exceptions.UserValidationException;
import pw.react.backend.services.UserService;
import pw.react.backend.web.UserDto;
import pw.react.backend.models.User;
import java.util.Collection;

@RestController
@RequestMapping(path = UserController.USERS_PATH)
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    static final String USERS_PATH = "/users";

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(oneOf = UserDto.class))}
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Something went wrong"
            )
    })
    @PostMapping(path = "")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        try {
            User us= userService.validateAndSave(UserDto.convertToUser(user));
            if(us==null)
                throw new UserValidationException("username already exists");
            log.info("Password is not going to be encoded");
            return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.valueFrom(us));
        } catch (Exception ex) {
            throw new UserValidationException(ex.getMessage(), USERS_PATH);
        }
    }
    @Operation(summary = "get all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "list of users",
                    content = {@Content(mediaType = "application/json", schema = @Schema(allOf = UserDto.class))}
            ),
            @ApiResponse(
                    responseCode = "402",
                    description = "Something went wrong"
            )
    })
    @GetMapping(path = "")
    public ResponseEntity<Collection<UserDto>> GetUsers() {
        try{
            Collection<UserDto> Usrs=userService.GetAll().stream().filter(User::isAdmin).map(UserDto::valueFrom).toList();

            log.info("Password is not going to be encoded");
            return ResponseEntity.ok(Usrs);
        } catch (Exception ex) {
            throw new UserValidationException(ex.getMessage(), USERS_PATH);
        }
    }
}
