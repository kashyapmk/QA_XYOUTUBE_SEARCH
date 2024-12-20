package demo.wrappers;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     * 
    */

    ChromeDriver driver;
    WebDriverWait wait;

    public Wrappers(ChromeDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    } 


    public void NavigateToUrl(){
        try{
            if(!(driver.getCurrentUrl().equals("https://www.youtube.com/"))){
                driver.get("https://www.youtube.com/");
                wait.until(ExpectedConditions.urlContains("youtube"));
                Thread.sleep(5000);
            }

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public String getAboutMessage(){
        try{
            WebElement aboutButton = driver.findElement(By.xpath("//a[text()='About']"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", aboutButton);

            aboutButton.click();
            wait.until(ExpectedConditions.urlContains("about"));
            Thread.sleep(5000);

            StringBuilder sb = new StringBuilder();
            
            WebElement aboutYt = driver.findElement(By.xpath("//section[@class='ytabout__content']/h1"));
            sb.append(aboutYt.getText() + " ");

            List<WebElement> aboutTxt = driver.findElements(By.xpath("//section[@class='ytabout__content']/p"));
            for(WebElement wb: aboutTxt){
                sb.append(wb.getText()+" ");
            }

            return sb.toString().trim();

        }catch(Exception e){

            return ""+e;

        }


    }

    public void selectExploreOption(String option){
        try{

            WebElement exp = driver.findElement(By.xpath("//ytd-guide-entry-renderer/a[@title='"+option+"']/tp-yt-paper-item"));
            exp.click();

            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("channel"),
                ExpectedConditions.urlContains("feed")
            ));
            Thread.sleep(2500);


        }catch(Exception e){
            System.out.println(e);
        }

    }

    public String[] verifyTopSellingMovies(){

        try{
            
            //locate the section 
            WebElement parentSection = driver.findElement(By.xpath("//ytd-item-section-renderer[.//span[text()='Top selling']]"));

            //click on next button of the section located
            WebElement nextButton = parentSection.findElement(By.xpath(".//button[@aria-label='Next']"));
            while(nextButton.isDisplayed()){
                nextButton.click();
                Thread.sleep(2500);
            }

            //get the movie certificate text
            List<WebElement> filmCert = parentSection.findElements(By.xpath(".//ytd-grid-movie-renderer//div[contains(@class,'type-simple')]/p"));
            String filmCertificate = "";
            if(!filmCert.isEmpty()){
                WebElement wb1 = filmCert.get(filmCert.size()-1);
                filmCertificate = wb1.getText();

            }else{
                System.out.println("no films error !!!");
            }

            //get the movie category
            List<WebElement> filmCateg = parentSection.findElements(By.xpath(".//ytd-grid-movie-renderer//span[contains(@class,'movie-renderer-metadata')]"));
            String filmCategory = "";
            if(!filmCateg.isEmpty()){
                WebElement wb2 = filmCateg.get(filmCateg.size()-1);
                filmCategory = wb2.getText();

            }else{
                System.out.println("no films error !!!");
            }

            

            return new String[] {filmCertificate, filmCategory};
            

        }catch(Exception e){

            e.printStackTrace();
            return new String[] {"Error: " + e.getMessage()};
        }
    }

    public int songLikes = 0;
    public String songTitle;

    public void verifyMusic(){
        try{

            //first section of music channel
            WebElement parentElement = driver.findElement(By.xpath("//ytd-item-section-renderer[1]"));

            //locate next button and click until last song
            WebElement nextButton = parentElement.findElement(By.xpath(".//button[@aria-label='Next']"));
            while(nextButton.isDisplayed()){
                nextButton.click();
                Thread.sleep(2500);
            }

            //get the number of songs
            List<WebElement> numOfSongs = parentElement.findElements(By.xpath(".//yt-lockup-view-model//div[@class='badge-shape-wiz__text']"));
            int Likes = 0;
            if(!numOfSongs.isEmpty()){
                WebElement wbsong = numOfSongs.get(numOfSongs.size()-1);
                Likes = Integer.parseInt(wbsong.getText().split(" ")[0]);
                this.songLikes = Likes;
            }else{
                System.out.println("no music error!!!");
            }

            Thread.sleep(2000);

            //get the song title
            List<WebElement> titleOfSongs = parentElement.findElements(By.xpath(".//yt-lockup-view-model//h3"));
            String title = "";
            if(!titleOfSongs.isEmpty()){
                WebElement wbtitle = titleOfSongs.get(titleOfSongs.size()-1);
                title = wbtitle.getAttribute("title");
                this.songTitle = title;
            }else{
                System.out.println("no music error!!!");
            }


        }catch(Exception e){
            System.out.println(e);
        }
        

    }

    public void verifyLatestNews(){
        try{

            //locate latest news posts section
            List<WebElement> parentElements = driver.findElements(By.xpath("//ytd-rich-section-renderer[.//span[text()='Latest news posts']]//ytd-rich-item-renderer"));

            int likesSum = 0;

            System.out.println("outloop: "+ parentElements.size());
            for(WebElement parentElement: parentElements){
                //get the title text from latest news posts section
                WebElement allLatestNewsTitle = parentElement.findElement(By.xpath(".//div[@id='author']/a/span"));
                
                    String title = allLatestNewsTitle.getText();
                    System.out.println("Latest News Post title: " + title);
                    Thread.sleep(2000);
                

                //get the body text from latest news posts section
                WebElement allLatestNewsPost = parentElement.findElement(By.xpath(".//span[@dir='auto'][position()=1]"));
                
                    String body = allLatestNewsPost.getText();
                    System.out.println("Latest News Post body: " + body);
                    Thread.sleep(2000);
                

                //get the likes from latest news posts section
                WebElement allLatestNewsLikes = parentElement.findElement(By.xpath(".//span[@id='vote-count-middle']"));

                    //format string to digits
                    String likeStr = allLatestNewsLikes.getText().trim();
                    if (likeStr.endsWith("K")) {
                        likeStr = likeStr.replace("K", "");
                        // Parse and convert to integer
                        likesSum += (int) (Double.parseDouble(likeStr) * 1000);
                    } else if (likeStr.endsWith("M")) {
                        likeStr = likeStr.replace("M", "");
                        // Parse and convert to integer
                        likesSum += (int) (Double.parseDouble(likeStr) * 1000000);
                    } else {
                        // Directly parse the number
                        likesSum += Integer.parseInt(likeStr);
                    }
                    
                    
                

                
            }
            System.out.println("Latest News Post likes: " + likesSum);

        }catch(Exception e){
            System.out.println(e);
        }
        


    }




     
}
