using System.IO;

namespace Crawling
{
    internal class FileOutputStream : OutputStream
    {
        private FileStream fs;

        public FileOutputStream(FileStream fs)
        {
            this.fs = fs;
        }
    }
}