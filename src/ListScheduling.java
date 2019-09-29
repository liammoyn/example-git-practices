import java.util.Scanner;
import java.util.Collections;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.HashMap;

public class ListScheduling {

  static int MAX_MINUTE = 24*60;

  public static void main(String[] args) {
    ArrayList<Request> requests = new ArrayList<>();
    int lastNow = 0;
    int lastEnd = 0;

    Scanner scan = new Scanner(System.in);
    while (scan.hasNext()) {
      String line = scan.nextLine();

      if (line.startsWith("cancel")) {
        //Cancel a booking
        Request request = new Request(line.split(" ")[1]);
        requests.remove(request);
        Collections.sort(requests);
      } else if (line.contains(",")) {
        //Schedule a booking
        Request request = new Request(line);

        //Only add request if we haven't already passed its start time
        if (request.startMinute >= lastEnd) {
          requests.add(request);
          Collections.sort(requests);
        }
      } else {
        //Time Passing
        int now = Request.toMinutes(line);

        for (int i = 0; i < requests.size(); i++) {
          Request request = requests.get(i);

          //If request start time has already passed then skip
          if (request.startMinute < lastNow) {
            continue;
          }

          //If request should be handled at this time then process
          if (request.startMinute <= now) {
            //If this request is valid then print and update new time for next entry
            if (request.startMinute >= lastEnd) {
              System.out.println(request);
              lastEnd = request.endMinute;
            }
          } else {
            break;
          }
        }

        lastNow = now;
      }
    }

    //Handle any unprocessed requests
    for (int i = 0; i < requests.size(); i++) {
      Request request = requests.get(i);

      if (request.startMinute < lastNow) {
        continue;
      }

      if (request.startMinute >= lastEnd) {
        System.out.println(request);
        lastEnd = request.endMinute;
      }
    }
  }

  // Here's a handy class for requests that implements some tricky bits for
  // you, including a compareTo method (so that it's a Comparable, so that
  // it can be sorted with Collections.sort()), a hashCode method
  // (so that identical time ranges are treated as identical keys in
  // a hashMap), and an overlaps() method (which students have often
  // gotten wrong in the past by omitting cases).  Parsing, equals(), and
  // toString() are also handled for you.
  public static class Request implements Comparable {
    private int startMinute;
    private int endMinute;

    // Constructor that takes the request format specified in the
    // assignment (startTime,endTime using 24-hr clock)
    public Request(String inputLine) {
      String[] inputParts = inputLine.split(",");
      startMinute = toMinutes(inputParts[0]);
      endMinute = toMinutes(inputParts[1]);
    }

    // Convert time to an integer number of minutes; mostly
    // for internal use by the class
    private static int toMinutes(String time) {
      String[] timeParts = time.split(":");
      int hour = Integer.valueOf(timeParts[0]);
      int minute = Integer.valueOf(timeParts[1]);
      return hour*60 + minute;
    }

    // Don't feel like you need to use these accesssors, but they're
    // here in case I decide to change the internal representation
    // someday
    public int getStartMinute() {
      return startMinute;
    }

    public int getEndMinute() {
      return endMinute;
    }

    // Did you know toString() gets called automatically when your object
    // is put in a situation that expects a String?
    public String toString() {
      return timeToString(startMinute) + "," + timeToString(endMinute);
    }

    // Mostly for use by toString() - format number of minutes as 24hr time
    private static String timeToString(int minutes) {
      if ((minutes % 60) < 10) {
        return (minutes/60) + ":0" + (minutes%60);
      }
      return (minutes/60) + ":" + (minutes%60);
    }

    // Check whether two Requests overlap in time.
    public boolean overlaps(Request r) {
      // Four kinds of overlap...
      // r starts during this request:
      if (r.getStartMinute() >= getStartMinute() &&
          r.getStartMinute() < getEndMinute()) {
        return true;
      }
      // r ends during this request:
      if (r.getEndMinute() > getStartMinute() &&
          r.getEndMinute() < getEndMinute()) {
        return true;
      }
      // r contains this request:
      if (r.getStartMinute() <= getStartMinute() &&
          r.getEndMinute() >= getEndMinute()) {
        return true;
      }
      // this request contains r:
      if (r.getStartMinute() >= getStartMinute() &&
          r.getEndMinute() <= getEndMinute()) {
        return true;
      }
      return false;
    }

    // Allows use of Collections.sort() on this object
    // (implements Comparable interface)
    public int compareTo(Object o) {
      if (!(o instanceof Request)) {
        throw new ClassCastException();
      }
      Request r = (Request)o;
      if (r.getEndMinute() > getEndMinute()) {
        return -1;
      } else if (r.getEndMinute() < getEndMinute()) {
        return 1;
      } else if (r.getStartMinute() < getStartMinute()) {
        // Prefer later start times, so sort these first
        return -1;
      } else if (r.getStartMinute() > getStartMinute()) {
        return 1;
      } else {
        return 0;
      }
    }

    // The hash function for the hashMap, without which our scheme
    // of counting requests with the same range would not work.
    // You don't need to call this yourself; it's used every time
    // get(), contains(), or something similar is called
    public int hashCode() {
      return MAX_MINUTE*startMinute + endMinute;
    }

    // Determine whether two objects are equal.  If we're not in a hashing
    // context, other generics will use this to implement functions like
    // contains() or remove().
    public boolean equals(Object o) {
      if (!(o instanceof Request)) {
        return false;
      }
      Request that = (Request) o;
      return (this.startMinute == that.startMinute && this.endMinute == that.endMinute);
    }

  }
}
