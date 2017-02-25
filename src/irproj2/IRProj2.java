/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irproj2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;


/**
 *
 * @author Ajay-Pc
 */
public class IRProj2 
{
    public static Map<String,LinkedList> map=new HashMap<>();                   //Map for storing Postings Lists
        /**
         * @param args the command line arguments
         * @throws java.io.IOException
         */
    public static void GetPostings(List<String> Qterms,BufferedWriter bufferedwriter) throws IOException    //Function for Getting Postings List
    {
        for (int s=0;s<Qterms.size();s++)                                       //Gives the postings List for Terms in Qterms
        {
                bufferedwriter.write("Get postings");
                bufferedwriter.newLine(); 
                bufferedwriter.write(Qterms.get(s));                            //Writes terms to the file
                bufferedwriter.newLine();
                System.out.println("Get postings");
                System.out.println(Qterms.get(s));                              //prints terms
                //String st = Qterms.;
                LinkedList<Integer> post = map.get(Qterms.get(s));
                bufferedwriter.write("Postings List: "+post.toString().replaceAll("\\[|\\]|,","")); //To remove braces and commas from the Linked List
                bufferedwriter.newLine();
                System.out.println("Postings List: "+post.toString().replaceAll("\\[|\\]|,","")); //The terms posting list
            }
            
    }
    
    public static void TaatAnd(List<String> Qterms,List<String> Qterms2,BufferedWriter bufferedwriter) throws IOException   //Function for performing TaatAnd operation
    {
            String stAND = Qterms.get(0);
            LinkedList<Integer> first_termlis = map.get(stAND);
            List<Integer> first_termlist = new ArrayList();
            first_termlist.addAll(first_termlis);
            LinkedList<Integer> resultAND = new LinkedList();
            //LinkedList<Integer> result = post;
            int ComparisonAND=0;
          //  Boolean found_boolAND=false;
            for(int e = 1 ; e < Qterms2.size(); e++)                  //TAAT AND Operation
            {   int w = 0,q = 0;
                String query = Qterms2.get(e);
                LinkedList<Integer> new_termlist = map.get(query);    //Store query postings list in new_termlist
                
                while(q<first_termlist.size() && w<new_termlist.size()) //Loop for Comparison
                {   
                 
                    if(first_termlist.get(q)==new_termlist.get(w))      //Logic for And
                    {
                        ComparisonAND++;
                        resultAND.add(first_termlist.get(q));
                        w++;q++;
                    }
                    else if (first_termlist.get(q)<new_termlist.get(w))
                    {
                        ComparisonAND++;
                        q++;
                    }
                    else
                    {
                        ComparisonAND++;
                        w++;
                    }
                }
                first_termlist = resultAND;                                     //Storing Intermediate Results
            }
            Collections.sort(resultAND);
            System.out.println("TaatAnd");
            bufferedwriter.write("TaatAnd");
            bufferedwriter.newLine();
            System.out.println(Qterms.toString().replaceAll("\\[|\\]|,",""));   
            bufferedwriter.write(Qterms.toString().replaceAll("\\[|\\]|,","")); //Writes the terms list to the file
            bufferedwriter.newLine();
            if(first_termlist.isEmpty())                                        //Condition for empty result
            {
            System.out.println("Results: empty");
            bufferedwriter.write("Results: empty");
            bufferedwriter.newLine();
            }
            else
            {System.out.println("Results: "+first_termlist.toString().replaceAll("\\[|\\]|,",""));
            bufferedwriter.write("Results: "+first_termlist.toString().replaceAll("\\[|\\]|,","")); //Writing the result in the file
            bufferedwriter.newLine();
            }
            
            System.out.println("Number of documents in results: "+first_termlist.size());           //'TAAT AND' Result
            bufferedwriter.write("Number of documents in results: "+first_termlist.size());
            bufferedwriter.newLine();
            System.out.println("Number of comparisons: "+ComparisonAND);
            bufferedwriter.write("Number of comparisons: "+ComparisonAND);                          //Prints number of comparisons
            bufferedwriter.newLine();
    }
    
    public static void TaatOr(List<String> Qterms,BufferedWriter bufferedwriter) throws IOException //Taat Or Operation
    {
        String st = Qterms.get(0);
            LinkedList<Integer> result1 = map.get(st);
            List<Integer> result2 = new ArrayList();
            result2.addAll(result1);
            
            int Comparison=0;
           // Boolean found_bool=false;         
            for(int r = 1; r < Qterms.size(); r++)                  //TAAT OR Operation
            {   LinkedList<Integer> result3 = new LinkedList();
                int ww = 0,qq = 0;
                String query = Qterms.get(r);
                LinkedList<Integer> new_list = map.get(query);
                
                while(qq<result2.size() && ww<new_list.size())                  //Taat Or Logic Begins
                { 
                   
                    if(result2.get(qq)== new_list.get(ww))                      //Comparison between terms
                        {
                            Comparison++;
                            result3.add(result2.get(qq));
                            ww++;qq++;
                        }
                    else if (result2.get(qq)<new_list.get(ww))                  
                        {   
                            result3.add(result2.get(qq));                       //Add the term to the intermediate result
                            Comparison++;
                            qq++;
                        }
                    else
                        {   
                            result3.add(new_list.get(ww));                      //Add the term to the intermediate result
                            Comparison++;
                            ww++;
                        }
                }
                if(qq<result2.size())                                           //Finish the iterations until end of list
                {
                    while(qq<result2.size())
                    {
                        result3.add(result2.get(qq));                           //Saving the intermediate result
                        qq++;
                    }
                }
                if(ww<new_list.size())                                          //Finish the iterations until end of list
                {
                    while(ww<new_list.size())
                    {
                        result3.add(new_list.get(ww));                          //Saving the intermediate result
                        ww++;
                    }
                }
                result2 = result3;                                              //Final result
                
            }    
            Collections.sort(result2);                                          //Sort the Result
            System.out.println("TaatOr");
            bufferedwriter.write("TaatOr");
            bufferedwriter.newLine();
            System.out.println(Qterms.toString().replaceAll("\\[|\\]|,",""));   
            bufferedwriter.write(Qterms.toString().replaceAll("\\[|\\]|,","")); //Writes the query terms to the file
            bufferedwriter.newLine();
            if(result2.isEmpty())
            {
            System.out.println("Results: empty");               
            bufferedwriter.write("Results: empty");                             //Result Empty Condition
            }
            else{
            System.out.println("Results: "+result2.toString().replaceAll("\\[|\\]|,",""));
            bufferedwriter.write("Results: "+result2.toString().replaceAll("\\[|\\]|,",""));    //Write Final Result to the file
            bufferedwriter.newLine();
            }
            System.out.println("Number of documents in results: "+result2.size());           
            bufferedwriter.write("Number of documents in results: "+result2.size());         //Write number of documents to the file 
            bufferedwriter.newLine();
            System.out.println("Number of comparisons: "+Comparison);                       
            bufferedwriter.write("Number of comparisons: "+Comparison);         //Write the number of comparisons made
            bufferedwriter.newLine();        
    }    
    public static void DaatAnd(List<String> Qterms,BufferedWriter bufferedwriter) throws IOException    //DaatAnd Operation
    {
            List<LinkedList<Integer>> postingListsAnd = new LinkedList<LinkedList<Integer>>();          //Create a List for intermediate result storing and comparison operations
            int validAnd=0,ComparisonDAnd=0;
            Integer maxAnd;
            LinkedList<Integer> resultAnd = new LinkedList<Integer>();          //List to store result 
            int size = Qterms.size();
            Integer TermsNow[] = new Integer[size];
            Iterator[] iteratorAnd = new Iterator[Qterms.size()];               //Iterator to be used as a pointer to the lists for comparison purposes
            
            for (int l = 0; l < Qterms.size(); l++) 
            {
                String query = Qterms.get(l);
                postingListsAnd.add(validAnd,map.get(query));
                iteratorAnd[l] = map.get(query).iterator();                     //Place all iterators to first starting point
            }
            
            for(int p=0;p<Qterms.size();p++)
            {
                TermsNow[p]=(Integer) iteratorAnd[p].next();
            }
              maxAnd = TermsNow[0];                                             //Initialize max to first term
         
        while(1==1)                                                             //Run Infinitely
        {
             int ComparisonD=0;
             for(int z = 0; z<size; z++)
             {  
                 if(maxAnd.compareTo(TermsNow[z]) <0)                           //Compare max with current term and update max accordingly
                 {  
                     ComparisonDAnd++;
                     maxAnd = TermsNow[z];
                 }
                 else if(maxAnd.compareTo(TermsNow[z])==0)                      //Term is present in the document
                 {
                     ComparisonD++;                                             
                     ComparisonDAnd++;
                 } 
             }
             int flag = 1;
             if(ComparisonD==size)                                              //The term is present in all the documents
             {
                 resultAnd.add(maxAnd);                                         //Add the term to the result
                 for(int p=0;p<size;p++)
                {   
                    if(!iteratorAnd[p].hasNext())
                    {   
                        flag = 0;
                        break;
                    }
                    else
                    {
                        TermsNow[p]=(Integer)iteratorAnd[p].next();             //Move to the next document
                    }

                 }
                
             }
             else
             {
                for(int p=0;p<size;p++)
                {   
                        if(maxAnd.compareTo(TermsNow[p])!=0)
                        {   if(iteratorAnd[p].hasNext())                        //If terms are still present
                            {
                            TermsNow[p] = (Integer)iteratorAnd[p].next();       //Move to the next 
                            }
                            else
                            {
                                flag = 0;
                                break;
                            }
                        }
                }
             }
             if(flag==0)
             {
                 break;                                                         //Terminating Condition
             }
         }
         Collections.sort(resultAnd);                                           //Sort the result in an order
                    System.out.println("DaatAnd");
                    bufferedwriter.write("DaatAnd");
                    bufferedwriter.newLine();
                    System.out.println(Qterms.toString().replaceAll("\\[|\\]|,",""));
                    bufferedwriter.write(Qterms.toString().replaceAll("\\[|\\]|,",""));         //To output Query Terms to the File
                    bufferedwriter.newLine();
                    if(resultAnd.isEmpty())                                     //For Empty Result 
                    {
                    System.out.println("Results: empty");
                    bufferedwriter.write("Results: empty");
                    bufferedwriter.newLine();
                    }
                    else
                    {
                    System.out.println("Results: "+resultAnd.toString().replaceAll("\\[|\\]|,",""));    //Daat And Result
                    bufferedwriter.write("Results: "+resultAnd.toString().replaceAll("\\[|\\]|,",""));
                    bufferedwriter.newLine();
                    }
                    System.out.println("Number of documents in results: "+resultAnd.size());           //Number of Documents in Daat And 
                    bufferedwriter.write("Number of documents in results: "+resultAnd.size());
                    bufferedwriter.newLine();
                    System.out.println("Number of comparisons: "+ComparisonDAnd);                       //Number of Comparisons in Daat And
                    bufferedwriter.write("Number of comparisons: "+ComparisonDAnd);
                    bufferedwriter.newLine();

    }
    public static void DaatOr(List<String> Qterms,BufferedWriter bufferedwriter) throws IOException       //Daat Or Operation
    {
          List<Integer> resultDor = new ArrayList();
            //resultDOR.addAll(resultDor);
            List<LinkedList<Integer>> postingLists = new LinkedList<LinkedList<Integer>>();
            int valid=0,ComparisonDOR=0,max=0,DocumentsDone=0,point;
            int[] listIndex = new int[Qterms.size()];   //pointer to postinglists to point for comparison purposes
            for (int l = 0; l < Qterms.size(); l++) 
            {
                String query = Qterms.get(l);
                postingLists.add(valid,map.get(query));
                listIndex[valid] = 0;
                valid++;
            }
            for (;;)                                                            //Run infinitely
            {
            for (point = 0; point < valid; point++)                             //For all the documents
            {
                if(listIndex[point]<postingLists.get(point).size())             
                {  
                    if(max < postingLists.get(point).get(listIndex[point]))     //Compare max with the current term and update accordingly   
                    {   ComparisonDOR++;
                        max = postingLists.get(point).get(listIndex[point]);
                        resultDor.add(postingLists.get(point).get(listIndex[point]));       //Add the new term to the result
                    }
                    else if(max > postingLists.get(point).get(listIndex[point]))        //Check for duplicate terms 
                    {
                        
                        boolean present=false;
                        for (int r = 0; r < resultDor.size(); r++)              //Run over result size
                        {
                        ComparisonDOR++;
                        if (postingLists.get(point).get(listIndex[point]) == resultDor.get(r).intValue())   //Check for duplicate terms in the result
                        {
                           present=true;                                        //Term is duplicate
                            break;
                        }
                        }
                        if(!present)                                            //If the term is not duplicate
                        {
                            resultDor.add(postingLists.get(point).get(listIndex[point]));   //Add term to the result
                        }
                    }
                    listIndex[point]++;    
                    if (postingLists.get(point).size() == listIndex[point])     //Once a document is finished increase the number of docs traversed
                    {
                        DocumentsDone++;
                    }
                }
                
            }
            if(DocumentsDone==valid)                                            //If all the iterations are done
                {   
                    Collections.sort(resultDor);                                //Sort the result in an order
                    System.out.println("DaatOr");
                    bufferedwriter.write("DaatOr");
                    bufferedwriter.newLine();
                    System.out.println(Qterms.toString().replaceAll("\\[|\\]|,",""));
                    bufferedwriter.write(Qterms.toString().replaceAll("\\[|\\]|,",""));         //Write Terms to the file
                    bufferedwriter.newLine();
                    if(resultDor.isEmpty())                                     //To check for empty result 
                    {
                    System.out.println("Results: empty");
                    bufferedwriter.write("Results: empty");                     
                    }
                    else{
                    System.out.println("Results: "+resultDor.toString().replaceAll("\\[|\\]|,",""));
                    bufferedwriter.write("Results: "+resultDor.toString().replaceAll("\\[|\\]|,",""));      //Write the actual result to the file
                    bufferedwriter.newLine();  
                    }
                    System.out.println("Number of documents in results: "+resultDor.size());           //Number of documents in the result
                    bufferedwriter.write("Number of documents in results: "+resultDor.size());
                    bufferedwriter.newLine();
                    System.out.println("Number of comparisons: "+ComparisonDOR);
                    bufferedwriter.write("Number of comparisons: "+ComparisonDOR);          //Number of Comparisons to file
                    bufferedwriter.newLine();
                    break;
                }
            }
    }

    public static void main(String[] args) throws IOException                   //Main Function
    {    
        String path = args[0];                                                  //Index Path as an argument
        //String path ="C:/Users/Ajay-Pc/Desktop/535/Projects/Project 2/index"; 
        String outputfname = args[1];                                           //Output File name as an argument
        //String outputfname = "output.txt";
        String filename = args[2];                                              //Input File name as an argument
        //String filename = "input.txt";        
        int i = 0;
        FileSystem fs = FileSystems.getDefault();
        Path path1 = fs.getPath(path);
        int numDocs,count=0;
        IndexReader reader = DirectoryReader.open(FSDirectory.open(path1));     //Create a Reader to read index from the given path
        Collection<String> c =  MultiFields.getIndexedFields(reader);
        File file= new File(outputfname);
        if(!file.exists())
        {
            file.createNewFile();
        }
        BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputfname), "UTF-8"));
        Fields f;
        Terms t;
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename),StandardCharsets.UTF_8));
        TermsEnum te;
        PostingsEnum pe;
        BytesRef term;
        c.remove("_version_");                                                  //Remove _version_ field from the collection
        c.remove("id");                                                         //Remove id field from the collection
        System.out.println(c);
        numDocs = reader.maxDoc();                                              //To count number of documents 
        //System.out.println(numDocs);
        f=MultiFields.getFields(reader);                                        //To get all the fields from the reader
        
        for(String s : c)                                                       //Loop runs for all the text_xx in the collection
        { 
            t=f.terms(s);
           te=t.iterator();

            while((term = te.next())!= null)                                    //To check for end of term pointer
            {   
            //   Terms.add(term.utf8ToString());
               count++;
               pe = MultiFields.getTermDocsEnum(reader, s, term);               
               //sSystem.out.print(term.utf8ToString() +"[");
               LinkedList<Integer> linkedlist = new LinkedList<>();
                while((pe.nextDoc() !=  PostingsEnum.NO_MORE_DOCS))
                {
                  //  System.out.print(pe.docID()+" ");
                    linkedlist.add(pe.docID());
                    map.put(term.utf8ToString(),linkedlist);                    //To put terms into the map
                }
            }
        }  
        try
        {
            //Scanner in = new Scanner(file,"UTF-8");
            String line = null;
            while((line = br.readLine())!=null)                                 //To read line for query terms
            {
                if(line.isEmpty())                                              //If empty query is present
                {
                   line = br.readLine();                                        
                }
                //String line = br.readLine();
                String[] query_terms = line.split(" ");                         //To store query terms into a string query_terms
                for(int k=0;k<query_terms.length;k++)
                {
                    query_terms[k] = query_terms[k].replace("\uFEFF","");
                }
                List<String> Qterms = Arrays.asList(query_terms);
                List<String> Qterms2 = new ArrayList() ;
                boolean flag1= true;
                for(int k=0;k<Qterms.size();k++)                                //To create a list without Duplicate Querys
                {
                    if(k==0)
                    {
                        Qterms2.add(Qterms.get(k));
                    }   
                    else
                    {
                        for(int j=0;j<Qterms2.size();j++)
                        {
                            if(Qterms2.get(j)==Qterms.get(k))                   //Check for duplicate Query Term
                            { 
                                flag1=false;
                                break;
                            }
                            if(flag1!=false)
                            {
                                Qterms2.add(Qterms.get(k));
                                flag1 = true;
                            }
                
                        }
                    }
                }
               
                GetPostings(Qterms,bufferedwriter);
                //-------------BEGIN TAAT AND--------------//
                TaatAnd(Qterms,Qterms2,bufferedwriter);
                //-------------BEGIN TAAT OR---------------//
                TaatOr(Qterms,bufferedwriter);
                //-------------BEGIN DAAT AND--------------//
                DaatAnd(Qterms,bufferedwriter);           
                //-------------BEGIN DAAT OR---------------//
                DaatOr(Qterms,bufferedwriter);
            }
            br.close();                                                         //Close BufferedReader
            bufferedwriter.close();                                             //Close BufferedWriter
        }
        catch(FileNotFoundException e) 
        {
        }            
    }
}