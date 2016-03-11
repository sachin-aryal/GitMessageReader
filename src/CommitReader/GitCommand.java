package CommitReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SACHIN on 3/3/2016.
 */
public class GitCommand {

    public Map<String,List<String>> getMessage(String commitId,String commitPath) throws FileNotFoundException{

        if(!new File(commitPath).exists())
            throw new FileNotFoundException();

        String[] commandList = new String[]{"git log --format=%B -n 1","git diff-tree --no-commit-id --name-status -r","git --no-pager log -1 --pretty=format:\"%an <%ae>\"",
                "git branch --contains","git show -s --format=%ci","git show"};
        List<String> commitList = new ArrayList<>();
        List<String> commitMessage = new ArrayList<>();
        List<String> modiOrDelList = new ArrayList<>();
        Map<String,List<String>> allList=new HashMap<>();
        for(String eachCommand:commandList){
            Process p = null;
            try {
                p = Runtime.getRuntime().exec(eachCommand+" "+commitId,null,new File(commitPath));
                BufferedReader r = null;
                r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                boolean add = true;
                while (true) {
                    try {
                        line = r.readLine();
                        if(line!=null) {
                            if (eachCommand.equals(commandList[0])) {
                                commitMessage.add(line);
                            } else if (eachCommand.equals(commandList[1])) {
                                modiOrDelList.add(line);
                            } else if (eachCommand.equals(commandList[2])) {
                                commitList.add("Author: " + line);
                            } else if (eachCommand.equals(commandList[3])) {
                                commitList.add("Branch: " + line);
                            } else if (eachCommand.equals(commandList[4])) {
                                commitList.add("Date: " + line);
                            }else if(eachCommand.equals(commandList[5])){
                                commitList.add(line);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (line == null) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        allList.put("commitMessage",commitMessage);
        allList.put("commitInfo",commitList);
        allList.put("filesChange",modiOrDelList);
        return allList;
    }

    public String passToFile(Map<String,List<String>> allList,String filePath){
        FileOperation operation = new FileOperation();
        for(String message:allList.get("commitInfo")){
            if(message.contains(GitMessage.errorMessagePatternOne)|| message.contains(GitMessage.errorMessagePatternTwo)||message.contains(GitMessage.errorMessagePatternThree)){
                return "The Commit Id is Incorrect or Directory is not a git repository";
            }else{
                if(operation.writeToFile(allList,filePath)){
                    return "Saved Successfully.";
                }else{
                    return "Detail with this Commit Id Already Exists.";
                }
            }
        }
        return "The Commit Id is Incorrect or Directory is not a git repository";
    }

    public String writeByCommitId(String commitId,String commitPath){
        Map<String,List<String>> allList = null;
        //f536e05337d45e61c38873a2fc1ab6fd3b2468e0
        // fa7c71a80496c594e0dc544cabbca1969ea6fc84
        //C:\Users\deerwalk\IdeaProjects\MorningClass
        try {
            allList = getMessage(commitId,commitPath);
            return passToFile(allList,commitPath);
        } catch (FileNotFoundException e) {
            return "Directory Not Exists.";
        }
    }

    public List<String> readByCommitId(String commitId,String commitPath){
        FileOperation fileOperation = new FileOperation();
        if(fileOperation.checkCommitId(commitId,commitPath)){
            return fileOperation.readCommitReport(commitId,commitPath);
        }
        return null;
    }



}