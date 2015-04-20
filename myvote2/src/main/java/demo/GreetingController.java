package demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    ModeratorService myModeratorService=new ModeratorService();
    //private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    int i=1;
    HashMap<Integer,Greeting> modMap=new HashMap<Integer, Greeting>();
    //private Date date =new Date();
    @RequestMapping(method=RequestMethod.POST,value={"/api/v1/moderators"},consumes= "application/json", produces="application/json")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ResponseEntity<Greeting> greeting(@RequestBody @Valid Greeting g) {
        // @RequestParam(value="name", defaultValue="NA") String name, @RequestParam(value="email", defaultValue="NA") String email, @RequestParam(value="password", defaultValue="NA") String password) {
        long tempId=counter.incrementAndGet();
        TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'dd:HH:mm.sss'Z'");
        df.setTimeZone(tz);
        String myLocalTime = df.format(new Date());
        //Moderator myModerator=new Moderator( name, email, password, myLocalTime);
        Greeting greet= myModeratorService.createModerator(g.getName(),g.getEmail(),g.getPassword());
        //modMap.put((int)tempId,greet);
        //i++;
        //return greet;
        return new ResponseEntity<Greeting>(greet, HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.GET, value={"/api/v1/moderators/{moderator_Id}"})
    public Greeting getModerator(@PathVariable String moderator_Id)
    {
        int id = Integer.parseInt(moderator_Id);
        Greeting greet = myModeratorService.getModerator(id);
        return greet;
    }

    @RequestMapping(method=RequestMethod.PUT,value={"/api/v1/moderators/{moderator_Id}"})
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ResponseEntity<Greeting> updateModerator(@PathVariable String moderator_Id, @RequestBody Greeting g){
        String name="*";
        String email="*";
        String password="*";
        int id = Integer.parseInt(moderator_Id);
        if(g.getEmail()!=null && !(g.getEmail().equalsIgnoreCase("*"))) {
            email=g.getEmail();
        }
        if(g.getName()!=null && !(g.getName().equalsIgnoreCase("*"))) {
            name=g.getName();
        }
        if(g.getPassword()!=null && !(g.getPassword().equalsIgnoreCase("*"))) {
            password=g.getPassword();
        }
        if(g.getName()!=null || g.getEmail()!=null || g.getPassword()!=null) {
            Greeting myModerator=myModeratorService.updateModerator(id,name, email, password);
            return new ResponseEntity<Greeting>(myModerator, HttpStatus.CREATED);
        }
        //Greeting greet= modMap.get(id);
        //greet.setEmail(g.getEmail());
        //greet.setPassword(g.getPassword());
        //modMap.remove(id);
        //modMap.put(id,greet);
        //greet= Greeting((int) tempId, g.getName(), g.getEmail(), g.getPassword(), myLocalTime);
        //return greet;
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    //@RequestParam(value="email", defaultValue="NA") String email, @RequestParam(value="password", defaultValue="NA") String password)

}