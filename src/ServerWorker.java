import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker extends Thread
{
  private Socket client;
  private PrintWriter clientWriter;
  private BufferedReader clientReader;

  public ServerWorker(Socket client)
  {
    this.client = client;

    try
    {
      //          PrintWriter(OutputStream out, boolean autoFlushOutputBuffer)
      clientWriter = new PrintWriter(client.getOutputStream(), true);
    }
    catch (IOException e)
    {
      System.err.println("Server Worker: Could not open output stream");
      e.printStackTrace();
    }

    try
    {
      clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    }
    catch (IOException e)
    {
      System.err.println("Server Worker: Could not open input stream");
      e.printStackTrace();
    }
  }

  //Called by ServerMaster
  public void send(String msg)
  {
    System.out.println("ServerWorker.send(" + msg + ")");
    clientWriter.println(msg);
  }

  public void run()
  {
    String typedInput;
    while (true)
    {
      try
      {
        typedInput = clientReader.readLine();
        String[] val = typedInput.split(" ");
        if (val[0].equals("b"))
        {
          ThneedStore.getStore().buyThneeds(Integer.parseInt(val[1]), Float.parseFloat(val[2]), val[3]);
        }
        else if (val[0].equals("s"))
        {
          ThneedStore.getStore().sellThneeds(Integer.parseInt(val[1]), Float.parseFloat(val[2]), val[3]);
        }
      }
      catch (IOException e)
      {
        System.err.println(e);
      }
    }
  }

}