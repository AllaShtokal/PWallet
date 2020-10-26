package com.shtokal.passs.service;

import com.shtokal.passs.dto.ChangeUserPasswordRequest;
import com.shtokal.passs.dto.UserDTO;
import com.shtokal.passs.dto.UserDTORegister;
import com.shtokal.passs.dto.UserResponse;
import com.shtokal.passs.exceptions.LoginExistsException;
import com.shtokal.passs.model.User;
import com.shtokal.passs.repository.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;


@Service
public class UserServiceImplementation implements UserService {

    private static final String HMAC_SHA512 = "HmacSHA512";
    private static final String PEPPER = "qweqwf&678798sd6f7sd67cysd";
    private static final String KEY = "Welcome1";

    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Boolean login(UserDTO userDTO) {
        try {
            User user = userRepository.findByLogin(userDTO.getLogin());

            return checkIfPasswordsMatch(user.getIsPasswordKeptAsHash(),
                    user.getSalt(),
                    userDTO.getPassword(),
                    user.getPassword_hash());

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }


    private Boolean checkIfPasswordsSHA513Match(String salt, String password, String password_hash) {
        String inputPassHashSHA513 = calculateHashSHA512(PEPPER,salt, password);
        return checkIfPasswordsHashMatch(inputPassHashSHA513, password_hash);

    }

    private Boolean checkIfPasswordsHMASMatch(String password, String password_hash) {
        String inputPassHMAC = calculateHMAC(password, KEY);
        return checkIfPasswordsHashMatch(inputPassHMAC, password_hash);

    }

    private Boolean checkIfPasswordsHashMatch(String calculatedPaswordHash, String dbPasswordHash) {
        if (calculatedPaswordHash.equals(dbPasswordHash)) {
            return true;
        } else return false;


    }

    private Boolean checkIfPasswordsMatch(Boolean isPasswordKeptAsHash, String salt, String requestPass, String dbPassHash) {
        if (isPasswordKeptAsHash) {
            return checkIfPasswordsSHA513Match(salt, requestPass, dbPassHash);
        } else {
            return checkIfPasswordsHMASMatch(requestPass, dbPassHash);
        }
    }

    @Transactional
    @Override
    public Boolean changePassword(ChangeUserPasswordRequest userRequest) {

        User user = userRepository.findByLogin(userRequest.getLogin());
        if (checkIfPasswordsMatch(user.getIsPasswordKeptAsHash(),
                user.getSalt(),
                userRequest.getOldPassword(),
                user.getPassword_hash())
        ) {
            user.setIsPasswordKeptAsHash(userRequest.getIsPasswordSavedAsHash());
            String newPasswordHash = generateNewHashForUser(
                    user,
                    userRequest.getIsPasswordSavedAsHash(),
                    userRequest.getNewPassword());
            user.setPassword_hash(newPasswordHash);
            return true;
        } else return false;

    }

    private String generateNewHashForUser(User user, Boolean isPasswordSavedAsHash, String password) {
        if (isPasswordSavedAsHash) {
            String salt = generateSalt();
            user.setSalt(salt);
            return calculateHashSHA512(PEPPER, salt, password);
        } else return calculateHMAC(password, KEY);

    }

    @Transactional
    @Override
    public UserResponse add(UserDTORegister userDTORegister) throws LoginExistsException {
//        if (loginExist(userDTO.getLogin())) {
//            throw new LoginExistsException(
//                    "There is an account with that login:" + userDTO.getLogin()
//            );
//
//        }
        User user = new User();
        user.setLogin(userDTORegister.getLogin());
        user.setIsPasswordKeptAsHash(userDTORegister.getIsPasswordSavedAsHash());

        if (userDTORegister.getIsPasswordSavedAsHash()) {
            String salt = generateSalt();
            user.setSalt(salt);
            user.setPassword_hash(calculateHashSHA512(PEPPER,salt, userDTORegister.getPassword()));
        } else {

            user.setPassword_hash(calculateHMAC(userDTORegister.getPassword(), KEY));
        }


        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getId().toString());
        userResponse.setUserLogin(user.getLogin());
        return  userResponse;


    }


    private String calculateHMAC(String text, String key) {
        Mac sha512Hmac;
        String result = "";
        try {
            final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(text.getBytes(StandardCharsets.UTF_8));
            // Can either base64 encode or put it right into hex
            result = java.util.Base64.getEncoder().encodeToString(macData);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }


    private String calculateHashSHA512(String pepper, String salt, String text) {

        return calculateSHA512(pepper + salt + text);
    }


    private String calculateSHA512(String text) {
        try {
            //get an instance of SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //calculate message digest of the input string  - returns byte array
            byte[] messageDigest = md.digest(text.getBytes());
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
            // Convert message digest into hex value
            String hashtext = no.toString(16);
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            // return the HashText
            return hashtext;
        }
        // If wrong message digest algorithm was specified
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private Boolean loginExist(String login) {

        return userRepository.findByLogin(login) != null;
    }


    public String generateSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        String encodedSalt = Base64.encodeBase64String(salt);
        return encodedSalt;
    }

}
