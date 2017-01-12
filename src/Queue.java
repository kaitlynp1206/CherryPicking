/**
 * Created by Elizabeth Ip on 2017-01-10.
 */
public class Queue<E>{
    private Node<E>head;

    //determine if the queue is empty
    public boolean isEmpty(){
        Node<E>temp=head;

        if(temp==null){
            return true;
        }
        return false;
    }

    //add an item to the end of the queue
    public void enqueue(E item){
        Node<E>temp=head;
        Node<E>insert, next;
        boolean done=false;

        if(head==null){ //if queue is empty
            head=new Node<E>(item, null);//start a head node
        }else{//if queue is not empty
            insert=new Node<E>(item, null);

            while(temp.getNext()!=null && !done){//iterate through queue until you get to the end
                temp=temp.getNext();
            }
            if(!done){
                temp.setNext(insert);
            }

        }
    }

    //removes a node from the top of the queue
    public E dequeue(){
        Node<E> temp=head;

        head=head.getNext();
        return temp.getItem();
    }//end dequeue
}//end class