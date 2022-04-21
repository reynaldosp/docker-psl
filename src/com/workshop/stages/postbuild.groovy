#!/usr/bin/groovy
package com.workshop.stages
import com.workshop.Pipeline
def merge(Pipeline p){
  println "===========\u001b[44mMerging PR to base branch\u001b[0m=========="
  println "\u001b[36mMerge method using : \u001b[0mMerge..."
  def pr_merge_response = httpRequest authentication: 'github-personal', httpMode: 'PUT', url: "https://api.github.com/repos/${p.git_user}/${p.repository_name}/pulls/${p.pr_num}/merge", validResponseCodes: '200,405,409', wrapAsMultipart: false
  def parsed_pr_merge_response = readJSON text: "${pr_merge_response.content}"
  if (parsed_pr_merge_response.containsKey("merged")){
      if ("${parsed_pr_merge_response['merged']}" == "true") {
          println "\u001b[32mMerging Success !!!\u001b[0m"
          println "Message : \u001b[32m${parsed_pr_merge_response['message']}"
      } else {
          println "\u001b[31mMerge Failed!!!\u001b[0m"
          println "Error message : \u001b[31m${parsed_pr_merge_response['message']}\u001b[0m"
          error "Failed to merge, ${parsed_pr_merge_response['message']}"
      }
   } else {
       def pr_check_response = httpRequest authentication: 'github-personal', httpMode: 'GET', url: "https://api.github.com/repos/${p.git_user}/${p.repository_name}/pulls/${p.pr_num}", validResponseCodes: '200,405,409', wrapAsMultipart: false
       def parsed_pr_check_response = readJSON text: "${pr_check_response.content}"
 
       if ("${parsed_pr_check_response['merged']}" == "true") {
           println "PR Already Merged, Will Continue the Pipeline"
       }else {
           println "\u001b[31mMerge Failed!!!\u001b[0m"
           println parsed_pr_merge_response   
           println "Error message : \u001b[31m${parsed_pr_merge_response['message']}\u001b[0m"      
           error "Failed to merge, ${parsed_pr_merge_response['message']}"
       }
  }
  println "==============================================\n\n"
}

