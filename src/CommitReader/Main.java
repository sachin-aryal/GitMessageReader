package CommitReader;

import java.util.Scanner;

/**
 * Created by SACHIN on 3/3/2016.
 */
public class Main {
    public static void main(String[] args) {
        GitCommand command = new GitCommand();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter the Commit Id:");
        String commitId = scanner.nextLine();
        System.out.println("Enter Directory Path");
        String commitPath = scanner.nextLine();

        System.out.println(command.writeByCommitId(commitId,commitPath));//Write to file and return a message if
                                                                            // it successfully written or return error message if
                                                                                //  any errors occurred
                                                                                //Save the text file in the path that entered by 
                                                                                //user
        command.readByCommitId(commitId, commitPath);//Return a list of lines or null if it not find that commit id from file.

    }
}
