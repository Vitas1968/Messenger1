package Lesson6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConversArray
{
     Integer [] arr = {  2 ,2, 1,3,7,9,4,56,21,44}; // есть один и четыре
     Integer [] arr1 = {  2 ,2,3,7,9,55,25,45}; // нет ни одной четвеки или однерки
     Integer [] arr2 = {  2 ,2,4, 1,3,4,7,9,4,65,35,75}; // есть единица и две четверки


    public static void main(String[] args)
    {



        ConversArray conversArray=new ConversArray();
        int [] arr1=conversArray.conversToArray(conversArray.arr);

        for (int i = 0; i < arr1.length; i++)
        {
            System.out.print(arr1[i]+" ");
        }
        System.out.println();

        System.out.println(conversArray.checked(conversArray.arr));
    }

    int[] conversToArray(Integer[] arr)
    {
        ArrayList<Integer> list =new ArrayList( Arrays.asList(arr));
        if (list.contains(4))
        {
            int idx = list.lastIndexOf(4);
            List<Integer> lst=list.subList((idx+1),list.size());
            int tmp = (list.size()-1)-idx;
            int[] array = new int[tmp];
            for(int i = 0; i < lst.size(); i++) array[i] = lst.get(i);
            return array;
        }
        else  throw new RuntimeException("Not four");
    }
    boolean checked(Integer[] arr)
    {
        ArrayList<Integer> list =new ArrayList( Arrays.asList(arr));
        if (list.contains(4) || list.contains(1) ) return  true;
        return false;
    }
}
