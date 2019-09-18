using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;
using OpenQA.Selenium;
//using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.Chrome;
using System.Collections;
using System.IO;
using static System.Net.WebRequestMethods;
using System.Drawing;
using System.Drawing.Imaging;
using System.Runtime.InteropServices;


namespace Crawling
{
    class Program
    {
        private static object iterator;

        public static object OutputStream { get; private set; }
        public static object Save { get; private set; }
        
        static void Main(string[] args)
        {
            //학교 홈페이지 들어가기
            ChromeOptions options = new ChromeOptions();
            options.AddArguments("force-device-scale-factor=0.5");
            options.AddArguments("high-dpi-support=0.5");

            IWebDriver driver = new ChromeDriver(options);

            driver.Url = "https://www.kw.ac.kr/ko/life/notice.do";
            driver.Manage().Window.Maximize();

            Thread.Sleep(1000);
            IWebElement rows;

            //신규 게시글 15개 받아오기
            rows = driver.FindElement(By.ClassName("board-list-box"));
            string[] list = new string[15];
            string[] url_lists = new string[15];

            var coll = rows.FindElements(By.TagName("a"));
            int cnt = 0;


            foreach (var a in coll)
            {
                if (cnt == 15)
                    break;
                url_lists[cnt] = a.Text.ToString();
                Console.WriteLine(url_lists[cnt]);
                list[cnt] = a.Text.Replace("신규게시글", " ");
                cnt++;
            }

            //공지사항 제목과 url을 info.txt에 쓰기
            string localPath = Environment.GetFolderPath(Environment.SpecialFolder.MyDocuments) + "\\crawling";
            string localFileName = "info.txt";
            string sourceFile = Path.Combine(localPath, localFileName);
            FileStream fs = new FileStream(sourceFile, FileMode.OpenOrCreate,FileAccess.Write);
            string[] urls = new string[15];
            int cur = 0;

            //url_list: 공지사항 제목 신규게시글, list: 공지사항 제목, urls: 각각의 공지사항의 url

            for (int i=0;i<15;i++)
            {
                //각각의 공지사항 페이지에 들어감
                driver.FindElement(By.LinkText(url_lists[i])).Click();
                urls[i] = driver.Url;

                try
                {
                    //list_1: 자르지 않고 전체 화면 페이지 캡쳐
                    ((IJavaScriptExecutor)driver).ExecuteScript("window.scroll(0,370);");
                    Thread.Sleep(1000);
                    localFileName = i.ToString();
                    sourceFile = Path.Combine(localPath, localFileName);
                    ((ITakesScreenshot)driver).GetScreenshot().SaveAsFile(sourceFile);

                    Bitmap bmap = new Bitmap(300, 1000);
                    Graphics g = Graphics.FromImage(bmap);

                    Image img = Bitmap.FromFile(sourceFile);

                    Bitmap m1 = new Bitmap(img);
                    m1 = m1.Clone(
                            new Rectangle(643, 240, (img.Width - 955)/2, img.Height - 240),
                            System.Drawing.Imaging.PixelFormat.DontCare);
                    localFileName = i.ToString()+".png";
                    sourceFile = Path.Combine(localPath, localFileName);
                    //list: 자른 페이지 화면 png 파일로 저장
                    m1.Save(sourceFile,ImageFormat.Png);

                    //공지사항 한글로 encoding
                    System.Text.Encoding myEncoding = System.Text.Encoding.GetEncoding("ks_c_5601-1987");

                    //title: 공지사항 제목
                    byte[] title= myEncoding.GetBytes(list[i]);
                    int nwrite = (title).Length;

                    fs.Seek(cur, SeekOrigin.Begin);
                    fs.Write(title,0, nwrite);
                    cur += nwrite+1;

                    
                    byte[] nextline = myEncoding.GetBytes(Environment.NewLine);
                    fs.Write(nextline, 0, Environment.NewLine.Length);
                    cur += Environment.NewLine.Length-1;

                    //url: 공지사항의 url
                    byte[] url = myEncoding.GetBytes(urls[i]);
                    fs.Write(url, 0, urls[i].Length);
                    Console.WriteLine(urls[i]);
                    cur += urls[i].Length + 1;

               
                    fs.Write(nextline, 0, Environment.NewLine.Length);
                    cur += Environment.NewLine.Length - 1;
                }
                catch
                {

                }
                //다시 공지사항 목록페이지로 돌아가기
                driver.Url = "https://www.kw.ac.kr/ko/life/notice.do";
            }

            driver.Close();
            
        }
    }
}
