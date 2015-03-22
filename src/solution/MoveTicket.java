package solution;

import java.io.Serializable;

import scotlandyard.Colour;
import scotlandyard.Ticket;

public class MoveTicket extends Move implements Serializable{
  public MoveTicket(Colour colour, int target, Ticket ticket) {
    super(colour);
    this.target = target;
    this.ticket = ticket;
  }

  public final int target;
  public final Ticket ticket;


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MoveTicket) {
      MoveTicket that = (MoveTicket) obj;
      return (target == that.target
              && ticket.equals(that.ticket)
              && colour.equals(that.colour));
    }

    return false;
  }


    @Override
  public String toString() {
    return super.toString() + " " + this.target + " " + this.ticket;
  }
}
