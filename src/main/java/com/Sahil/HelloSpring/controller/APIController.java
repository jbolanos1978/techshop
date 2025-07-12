package com.Sahil.HelloSpring.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.Sahil.HelloSpring.model.Customers;
import com.Sahil.HelloSpring.model.Products;
import com.Sahil.HelloSpring.repository.CustomersRepository;
import com.Sahil.HelloSpring.repository.ProductsRepository;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller

@RequestMapping("/api")
public class APIController {

    @Autowired
    private CustomersRepository customerRepository;

    @Autowired
    private ProductsRepository productRepository;

    @Value("${JWT_SECRET}")
    private String jwtsecret;
    private Key getSigningKey() {
        byte[] keyBytes = jwtsecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private final int passwordlength = 12;

    private String generatePassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        String password = "";

        int i = 0;
        while (i < length ) {
            double randomIndex = Math.floor(Math.random() * characters.length());
            password += characters.charAt((int) randomIndex);
            i++;
        }
        return password;
    }

    @Value("${AWS_ACCESSKEY}")
    private String accessKey;
    @Value("${AWS_SECRETKEY}")
    private String secretKey;
    @Value("${AWS_REGION}")
    private String region;
    @Value("${AWS_BUCKET}")
    private String bucketName;

    @Value("${RESEND_APIKEY}")
    private String resendKey;

    @Value("${URL_ROOT}")
    private String URLROOT;

    private String emailTemplate(String header1, String header2, String message, String temporarypassword, String email, String URL) {
    return ("<div style='font-family: system-ui, sans-serif, Arial; font-size: 14px; color: #333; padding: 20px 14px; background-color: #f5f5f5;'>"+
        "<div style='max-width: 600px; margin: auto; background-color: #fff;'>"+
        "<div style='text-align: center; background-color: #fff; padding: 14px;'>"+
        "<h1>ECOMMERCE SITE</h1>"+
        "</div>"+
        "<div style='padding: 14px;'>"+
        "<h1 style='font-size: 22px; margin-bottom: 26px;'>" + header1 + "</h1>"+
        "<p>" + header2 + "</p>"+
        "<p>A temporary password has been generated for you.&nbsp; Please use the Link to login using that temporary password but also in the Login page select the 'Change Password' option to change it asap.</p>"+
        "<p>Temporary Password:</p>"+
        "<p>" + temporarypassword + "</p>"+
        "<p><a href='" + URL + "'>" + URL + "</a></p>"+
        "<p>" + message + "</p>"+
        "<p>Best regards,<br>ECommerce Site Team</p>"+
        "</div>"+
        "</div>"+
        "<div style='max-width: 600px; margin: auto;'>"+
        "<p style='color: #999;'>The email was sent to " + email + "<br>You received this email because you are registered with ECommerce Site.</p>"+
        "</div>"+
        "</div>");
    }


    @GetMapping("/products")
    public String getProducts (@RequestParam("search") Optional<String> searchParam, Model model){
        List<Products> vsearch = searchParam.map( param->productRepository.getContainingProduct(param) )
                .orElse(productRepository.findAll());
        model.addAttribute("products",vsearch);
        return "data";
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<String> readProduct (@PathVariable("productId") Long productId) {
        return ResponseEntity.of(productRepository.findById(productId).map(Products::getproductDescription));
    }

    @PostMapping("/products")
    public String addProduct (@ModelAttribute Products product, Model model) {
        Products producttoinsert = new Products();
        producttoinsert.setproductId(product.productId);
        producttoinsert.setproductDescription(product.productDescription);
        producttoinsert.setproductImage(product.productImage);
        productRepository.save(producttoinsert);
        model.addAttribute("products",productRepository.findAll());
        return "data";
    }

    @PostMapping("/products/{productId}")
    public String updateProduct (@PathVariable(value = "productId") Long productId, @ModelAttribute Products product, Model model) {
        Optional<Products> optionalproduct = productRepository.findById(productId);
        if (optionalproduct.isPresent()) {
            Products producttoupdate = optionalproduct.get();
            producttoupdate.setproductDescription(product.productDescription);
            producttoupdate.setproductImage(product.productImage);
            productRepository.save(producttoupdate);
        }
        model.addAttribute("products",productRepository.findAll());
        return "data";
    }

    @DeleteMapping("/products/{productId}")
    public String deleteProduct (@PathVariable(value = "productId") Long productId, Model model) {
        productRepository.deleteById(productId);
        model.addAttribute("products",productRepository.findAll());
        return "data";
    }

    @GetMapping("/customers")
    public String getCustomers (@RequestParam("search") Optional<String> searchParam, Model model){
        List<Customers> vsearch = searchParam.map( param->customerRepository.getContainingCustomer(param) )
                .orElse(customerRepository.findAll());
        model.addAttribute("customers",vsearch);
        model.addAttribute("temporarypassword","");
        return "customers";
    }

    @GetMapping("/customers/{Id}")
    public ResponseEntity<String> readCustomer (@PathVariable(value = "Id") String Id) {
        return ResponseEntity.of(customerRepository.findById(Id).map(Customers::getnombre));
    }

    @PostMapping("/customers")
    public String addCustomer (@ModelAttribute Customers customer, Model model) {
        Customers customertoinsert = new Customers();
        customertoinsert.setcedula(customer.cedula);
        customertoinsert.setnombre(customer.nombre);
        customertoinsert.setimageUrl(customer.imageUrl);
        customertoinsert.setemail(customer.email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userPassword", customer.password);
        Key vkey = getSigningKey();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(customer.cedula)
                .signWith(vkey, SignatureAlgorithm.HS256)
                .compact();
        customertoinsert.setpassword(token);
        if (customer.admin != null) {   
            customertoinsert.setadmin(true);
        }
        else {
            customertoinsert.setadmin(false);
        }
        customertoinsert.setfailedattempts(0L);
        customerRepository.save(customertoinsert);
        model.addAttribute("customers",customerRepository.findAll());
        model.addAttribute("temporarypassword","");
        return "customers";
    }

    @PostMapping("/customers/{Id}")
    public String updateCustomer (@PathVariable(value = "Id") String Id, @ModelAttribute Customers customer, Model model) {
        Optional<Customers> optionalcustomer = customerRepository.findById(Id);
        if (optionalcustomer.isPresent()) {
            Customers customertoupdate = optionalcustomer.get();
            customertoupdate.setnombre(customer.nombre);
            customertoupdate.setimageUrl(customer.imageUrl);
            customertoupdate.setadmin(customer.admin);
            customerRepository.save(customertoupdate);
        }
        model.addAttribute("customers",customerRepository.findAll());
        model.addAttribute("temporarypassword","");
        return "customers";
    }

    @DeleteMapping("/customers/{Id}")
    public String deleteCustomer (@PathVariable(value = "Id") String Id, Model model) {
        customerRepository.deleteById(Id);
        model.addAttribute("customers",customerRepository.findAll());
        model.addAttribute("temporarypassword","");
        return "customers";
    }

    @GetMapping("/customers/resetpassword/{Id}")
    public String resetCustomerPassword (@PathVariable(value = "Id") String Id, Model model) {
        Optional<Customers> optionalcustomer = customerRepository.findById(Id);
        String temporarypassword;
        if (optionalcustomer.isPresent()) {
            Customers customertoupdate = optionalcustomer.get();
            temporarypassword = generatePassword(passwordlength);
            Map<String, Object> claims = new HashMap<>();
            claims.put("userPassword", temporarypassword);
            Key vkey = getSigningKey();
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(Id)
                    .signWith(vkey, SignatureAlgorithm.HS256)
                    .compact();
            customertoupdate.setpassword(token);
            customertoupdate.setfailedattempts(0L);
            customerRepository.save(customertoupdate);

            Resend resend = new Resend(resendKey);
            CreateEmailOptions params = CreateEmailOptions.builder()
                .from("support@quinchojbv1978.com")
                .to(customertoupdate.getemail())
                .html(emailTemplate("You have requested a password change.","We received a request to reset the password for your account.&nbsp;","If you did not request this password reset, please ignore this email or let us know immediately. Your account remains secure.",temporarypassword,customertoupdate.getemail(),URLROOT+"/login"))
                .subject("Reset your password")
                .build();

            try {
                CreateEmailResponse data = resend.emails().send(params);
                System.out.println(data.getId());
                //model.addAttribute("message", "EMAIL SENT WITH TEMPORARY PASSWORD!");
            } catch (ResendException e) {
                System.out.println(e.getMessage());
                model.addAttribute("temporarypasswordmessage", "EMAIL FAILED PLEASE TRY AGAIN LATER!");
            }
            model.addAttribute("temporarypasswordmessage","Temporary Password for Customer " +  customertoupdate.getcedula() + " is: ");
            model.addAttribute("temporarypassword", temporarypassword);
            model.addAttribute("email", customertoupdate.getemail());
        }
        else {
            model.addAttribute("temporarypassword","");
        }
        model.addAttribute("customers",customerRepository.findAll());
        return "customers";
    }

    @PostMapping("/login")
    public RedirectView CustomerLogin (@ModelAttribute LoginCustomer customer, Model model, HttpServletResponse response, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!customer.forgotpassword.equals("")) {
            redirectAttributes.addAttribute("forgotpassword", customer.forgotpassword);
            redirectAttributes.addAttribute("URL", customer.URL);
            return new RedirectView("/api/forgotpassword");
        }
        Customers customerlogin = customerRepository.findByCedula(customer.cedula);
        if (customerlogin == null)
            return new RedirectView("/login?loginmessage=LOGIN FAILED!");
        Key vkey = getSigningKey(); 
        Jws<Claims> jwsClaims = Jwts.parserBuilder()
            .setSigningKey(vkey)
            .build()
            .parseClaimsJws(customerlogin.password);
        Claims claims = jwsClaims.getBody();
        if (claims.get("userPassword").toString().equals(customer.password)) {
            if (customerlogin.failedattempts < 3) {
                if (claims.get("userPassword").toString().equals(customer.newpassword))
                    return new RedirectView("/login?loginmessage=NEW PASSWORD MUST DIFFERENT THAN CURRENT PASSWORD!");
                if (!customer.newpassword.equals(customer.confirmpassword))
                    return new RedirectView("/login?loginmessage=NEW PASSWORD MUST BE EQUAL TO CONFIRM PASSWORD!");
                customerlogin.setfailedattempts(0L);
                if (!customer.newpassword.equals("")) {
                    Map<String, Object> newclaims = new HashMap<>();
                    newclaims.put("userPassword", customer.newpassword);
                    String token = Jwts.builder()
                        .setClaims(newclaims)
                        .setSubject(customer.cedula)
                        .signWith(vkey, SignatureAlgorithm.HS256)
                        .compact(); 
                    customerlogin.setpassword(token);
                }  
                customerRepository.save(customerlogin);
                Map<String, Object> newclaims = new HashMap<>();
                newclaims.put("userId", customer.cedula);
                String token = Jwts.builder()
                    .setClaims(newclaims)
                    .setSubject(customer.cedula)
                    .signWith(vkey, SignatureAlgorithm.HS256)
                    .compact();  
                Cookie httpOnlyCookie = new Cookie("techshopsitetoken", token);
                httpOnlyCookie.setHttpOnly(true);
                httpOnlyCookie.setPath("/");
                response.addCookie(httpOnlyCookie);
                model.addAttribute("customers",customerRepository.findAll());
                return new RedirectView("/showcustomers?customercedula=" + customer.cedula);
            }
            else {
                return new RedirectView("/login?loginmessage=3 FAILED ATTEMPTS - ACCOUNT IS NOW LOCKED!");
            }
        }
        else {
            customerlogin.setfailedattempts(customerlogin.failedattempts+1);
            customerRepository.save(customerlogin);
            return new RedirectView("/login?loginmessage=LOGIN FAILED!");
        }
    }

    @GetMapping("/forgotpassword")
    public String forgotPassword (@ModelAttribute("forgotpassword") String forgotpassword, @ModelAttribute("URL") String URL, Model model) {
        List<Customers> customeremail = customerRepository.findByEmail(forgotpassword);
        if (customeremail.size() != 1) {
            model.addAttribute("message", "EMAIL FAILED PLEASE TRY AGAIN LATER!");
            model.addAttribute("logincustomer", new LoginCustomer());
            return "login";
        }

        String temporarypassword = generatePassword(passwordlength);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userPassword", temporarypassword);
        Key vkey = getSigningKey();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject("Me")
                .signWith(vkey, SignatureAlgorithm.HS256)
                .compact();
        customeremail.get(0).setpassword(token);
        customeremail.get(0).setfailedattempts(0L);
        customerRepository.save(customeremail.get(0));
        
        Resend resend = new Resend(resendKey);
        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("support@quinchojbv1978.com")
                .to(forgotpassword)
                .html(emailTemplate("You have requested a password change.","We received a request to reset the password for your account.&nbsp;","If you did not request this password reset, please ignore this email or let us know immediately. Your account remains secure.",temporarypassword,forgotpassword,URLROOT+"/login"))
                .subject("Reset your password")
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            System.out.println(data.getId());
            model.addAttribute("message", "EMAIL SENT WITH TEMPORARY PASSWORD!");
        } catch (ResendException e) {
            System.out.println(e.getMessage());
            model.addAttribute("message", "EMAIL FAILED PLEASE TRY AGAIN LATER!");
        }

        model.addAttribute("logincustomer", new LoginCustomer());
        return "login";
    }

    @GetMapping("/logoff")
    public String logoffCustomer (HttpServletResponse response, Model model) {
        Cookie httpOnlyCookie = new Cookie("techshopsitetoken", null);
        httpOnlyCookie.setMaxAge(0);
        httpOnlyCookie.setHttpOnly(true);
        httpOnlyCookie.setPath("/");
        response.addCookie(httpOnlyCookie);
        model.addAttribute("logincustomer", new LoginCustomer());

        return "login";
    }

    @PostMapping(value="/customers/imageupload/{Id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadSingle(
        @RequestParam(name="file", required = false) MultipartFile file, @PathVariable(value = "Id") String Id, Model model
        ) throws IOException {
            String temporarypassword = generatePassword(passwordlength);
            String fileName = file.getOriginalFilename();
            fileName = Id + "-" + temporarypassword + fileName.substring(fileName.indexOf("."));
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
            file.transferTo(convFile);
            BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                .withRegion(region)
                                .build();
            PutObjectRequest request = new PutObjectRequest(bucketName, fileName, convFile);
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(request);
            String fileuploadedurl = s3Client.getUrl(bucketName, fileName).toString();
            System.out.println("Image Uploaded URL:"+fileuploadedurl);
            convFile.delete();

            Optional<Customers> optionalcustomer = customerRepository.findById(Id);
            if (optionalcustomer.isPresent()) {
                Customers customertoupdate = optionalcustomer.get();
                customertoupdate.setimageUrl(fileuploadedurl);
                customerRepository.save(customertoupdate);
            }
            model.addAttribute("customers",customerRepository.findAll());
            model.addAttribute("temporarypassword","");
            return "customers";
        }
}