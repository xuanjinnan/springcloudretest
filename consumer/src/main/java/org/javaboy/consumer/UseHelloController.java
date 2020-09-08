package org.javaboy.consumer;

import org.javaboy.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MultivaluedMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UseHelloController {
    @GetMapping("/hello1")
    public String hello1() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL("http://localhost:1113/hello");
            conn = (HttpURLConnection) url.openConnection();
            if (conn.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String s = br.readLine();
                br.close();
                return s;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error!";
    }

    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    @Qualifier("restTemplateOne")
    RestTemplate restTemplate;

    @GetMapping("/hello2")
    public String hello2() {
        HttpURLConnection conn = null;
        List<ServiceInstance> services = discoveryClient.getInstances("provider");
        ServiceInstance instance = services.get(0);
        String host = instance.getHost();
        int port = instance.getPort();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
//            URL url = new URL("http://localhost:1113/hello" );
        System.out.println(stringBuffer);
        String s = restTemplate.getForObject(stringBuffer.toString(), String.class);
        return s;
    }

    @Autowired
    @Qualifier("restTemplateTwo")
    RestTemplate balanceRestTemplate;

    @GetMapping("/hello3")
    public String hello3() {
        String s = balanceRestTemplate.getForObject("http://provider/hello", String.class);
        return s;
    }

    @GetMapping("hello4")
    public void hello4() {
        String s = balanceRestTemplate.getForObject("http://provider/hello2?name={1}", String.class, "javaboy");
        System.out.println(s);
        ResponseEntity<String> responseEntity = balanceRestTemplate.getForEntity("http://provider/hello2?name={1}", String.class, "javaboy");
        String body = responseEntity.getBody();
        System.out.println("body:" + body);
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("HttpStatus:" + statusCode);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        System.out.println("statusCodeValue:" + statusCodeValue);
        System.out.println("----------------header------------------");
        HttpHeaders headers = responseEntity.getHeaders();
        Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }

    @GetMapping("/hello5")
    public void hello5() throws UnsupportedEncodingException, URISyntaxException {
        //多个参数的传参方式
//       balanceRestTemplate.getForObject("http://provider/hello2?name={1}&age={2}", String.class, "javaboy", 32);
        String s = balanceRestTemplate.getForObject("http://provider/hello2?name={1}", String.class, "javaboy");
        System.out.println(s);
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        s = balanceRestTemplate.getForObject("http://provider/hello2?name={name}", String.class, map);
        System.out.println(s);
        // URI 形式中文必须转码
        String url = "http://provider/hello2?name=" + URLEncoder.encode("张三", "UTF-8");
        s = balanceRestTemplate.getForObject(new URI(url), String.class);
        System.out.println(s);
    }
    @GetMapping("/hello6")
    public void hello6(){
        MultiValueMap map = new LinkedMultiValueMap<String,Object>();
        map.add("username","javaboy");
        map.add("password","123");
        map.add("id",99);
        User user = balanceRestTemplate.postForObject("http://provider/user1", map, User.class);
        System.out.println(user);

        user.setId(98);
        user = balanceRestTemplate.postForObject("http://provider/user2", user, User.class);
        System.out.println(user);
    }
    @GetMapping("/hello7")
    public void hello7(){
        MultiValueMap map = new LinkedMultiValueMap<String,Object>();
        map.add("username","javaboy");
        map.add("password","123");
        map.add("id",99);
        URI uri = balanceRestTemplate.postForLocation("http://provider/register", map, User.class);
        System.out.println(uri);

        String s = balanceRestTemplate.getForObject(uri, String.class);
        System.out.println(s);
    }

    @GetMapping("/hello8")
    public void hello8(){
        MultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("id",99);
        map.add("username","javaboy");
        map.add("password","123");
        balanceRestTemplate.put("http://provider/user1",map);
        User user = new User();
        user.setId(100);
        user.setUsername("xuan");
        user.setPassword("456");
        balanceRestTemplate.put("http://provider/user2", user);
    }
    @GetMapping("/hello9")
    public  void hello9(){
        balanceRestTemplate.delete("http://provider/user1?id=11");
        balanceRestTemplate.delete("http://provider/user1?id={1}",22);
        balanceRestTemplate.delete("http://provider/user2/{1}",33);
    }

}
