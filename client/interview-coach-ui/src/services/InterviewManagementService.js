import api from './api'
import {getRole} from "./AuthService"
export async function getMyInterviews() {
    console.log("Get interviews in Interview Management service")
    const response = await api.get("/interviews/me")
    return response.data

    // return {

    //     interviews: [

    //         {
    //             interviewId: "INT-001",
    //             candidateEmail: "archit@gmail.com",
    //             interviewerEmail: "john@google.com",
    //             interviewStatus: "SCHEDULED",
    //             meetingLink: "https://meet.google.com/abc-defg-hij",
    //             interviewDate: "2026-07-10",
    //             interviewStartTime: "19:00",
    //             interviewEndTime: "20:00"
    //         },

    //         {
    //             interviewId: "INT-002",
    //             candidateEmail: "archit@gmail.com",
    //             interviewerEmail: "sarah@amazon.com",
    //             interviewStatus: "COMPLETED",
    //             meetingLink: "https://meet.google.com/demo-1",
    //             interviewDate: "2026-07-02",
    //             interviewStartTime: "18:00",
    //             interviewEndTime: "19:00"
    //         },

    //         {
    //             interviewId: "INT-003",
    //             candidateEmail: "archit@gmail.com",
    //             interviewerEmail: "mike@microsoft.com",
    //             interviewStatus: "CANCELLED",
    //             meetingLink: null,
    //             interviewDate: "2026-06-28",
    //             interviewStartTime: "20:00",
    //             interviewEndTime: "21:00"
    //         },

    //         {
    //             interviewId: "INT-004",
    //             candidateEmail: "archit@gmail.com",
    //             interviewerEmail: "emma@atlassian.com",
    //             interviewStatus: "SCHEDULED",
    //             meetingLink: "https://meet.google.com/demo-2",
    //             interviewDate: "2026-07-15",
    //             interviewStartTime: "17:00",
    //             interviewEndTime: "18:00"
    //         },

    //         {
    //             interviewId: "INT-005",
    //             candidateEmail: "archit@gmail.com",
    //             interviewerEmail: "david@oracle.com",
    //             interviewStatus: "COMPLETED",
    //             meetingLink: "https://meet.google.com/demo-3",
    //             interviewDate: "2026-06-20",
    //             interviewStartTime: "15:00",
    //             interviewEndTime: "16:00"
    //         }

    //     ]

    // };

}

export async function getInterviewersBySkills(skills) {
    /*
         @GetMapping("/interviewers")
    public ResponseEntity<List<InterviewerResponseDTO>> getInterviewers(
            @RequestParam List<String> skills,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(imService.getInterviewers(skills, pageNumber, pageSize));
    }

    */



    console.log("Searching for skills:",skills);
    const response = await api.get("/interviews/interviewers", {
        params : {
            skills : skills 
        }
    })

    return response.data

    // return {

    //     interviewers: [

    //         {
    //             fullName:
    //                 "John Doe",

    //             email:
    //                 "john@google.com",

    //             rating:
    //                 4.9,

    //             skills: [
    //                 "Java",
    //                 "Spring Boot",
    //                 "Kafka",
    //                 "System Design"
    //             ]
    //         },

    //         {
    //             fullName:
    //                 "Sarah Smith",

    //             email:
    //                 "sarah@amazon.com",

    //             rating:
    //                 4.8,

    //             skills: [
    //                 "Java",
    //                 "Redis",
    //                 "Microservices",
    //                 "AWS"
    //             ]
    //         },

    //         {
    //             fullName:
    //                 "Michael Johnson",

    //             email:
    //                 "michael@microsoft.com",

    //             rating:
    //                 4.7,

    //             skills: [
    //                 "Java",
    //                 "Spring Boot",
    //                 "Azure",
    //                 "Kafka"
    //             ]
    //         },

    //         {
    //             fullName:
    //                 "Emma Wilson",

    //             email:
    //                 "emma@atlassian.com",

    //             rating:
    //                 4.9,

    //             skills: [
    //                 "System Design",
    //                 "Redis",
    //                 "Java",
    //                 "Docker"
    //             ]
    //         },

    //         {
    //             fullName:
    //                 "David Brown",

    //             email:
    //                 "david@oracle.com",

    //             rating:
    //                 4.6,

    //             skills: [
    //                 "Spring Boot",
    //                 "MySQL",
    //                 "JPA",
    //                 "Java"
    //             ]
    //         },

    //         {
    //             fullName:
    //                 "Sophia Taylor",

    //             email:
    //                 "sophia@netflix.com",

    //             rating:
    //                 5.0,

    //             skills: [
    //                 "Kafka",
    //                 "Distributed Systems",
    //                 "Java",
    //                 "Microservices"
    //             ]
    //         },

    //         {
    //             fullName:
    //                 "James Anderson",

    //             email:
    //                 "james@uber.com",

    //             rating:
    //                 4.8,

    //             skills: [
    //                 "Redis",
    //                 "Kafka",
    //                 "System Design",
    //                 "Docker"
    //             ]
    //         },

    //         {
    //             fullName:
    //                 "Olivia Martin",

    //             email:
    //                 "olivia@linkedin.com",

    //             rating:
    //                 4.9,

    //             skills: [
    //                 "Java",
    //                 "Spring Boot",
    //                 "Redis",
    //                 "Microservices"
    //             ]
    //         }

    //     ]

    // };


}

export async function getAvailableSlots(email) {
    const response = await api.get("/slots/get", {
        params : {
            email : email 
        }
    })

    return response.data

    // return {

    //     slots: [

    //         {
    //             slotId: 1,
    //             date: "2026-07-10",
    //             startTime: "19:00",
    //             endTime: "20:00"
    //         },

    //         {
    //             slotId: 2,
    //             date: "2026-07-11",
    //             startTime: "20:00",
    //             endTime: "21:00"
    //         },

    //         {
    //             slotId: 3,
    //             date: "2026-07-12",
    //             startTime: "18:00",
    //             endTime: "19:00"
    //         }

    //     ]

    // };

}

export async function createInterviewRequest(slotId, interviewerEmail, topics, description) {
 
   const requestBody = {slotId, interviewerEmail, topics, description}
   console.log(requestBody)
   console.log(getRole())
    await api.post("/interviews/requests", requestBody)
    return "SUCCESS"

}


export async function getInterviewRequests() {
    const response = await api.get("interviews/requests/me")
    return {
        interviewRequests : response.data
    }

    // return {

    //     interviewRequests: [

    //         {
    //             requestId:
    //                 "1",

    //             slotId:
    //                 "101",

    //             description:
    //                 "Backend Java Interview",

    //             topics: [
    //                 "Java",
    //                 "Spring Boot",
    //                 "Kafka"
    //             ],

    //             status:
    //                 "PENDING",

    //             candidateEmail:
    //                 "archit@gmail.com",

    //             interviewerEmail:
    //                 "john@google.com",

    //             slotDate:
    //                 "2026-07-10",

    //             slotStartTime:
    //                 "19:00",

    //             slotEndTime:
    //                 "20:00"
    //         },

    //         {
    //             requestId:
    //                 "2",

    //             slotId:
    //                 "102",

    //             description:
    //                 "System Design Round",

    //             topics: [
    //                 "System Design",
    //                 "Redis"
    //             ],

    //             status:
    //                 "ACCEPTED",

    //             candidateEmail:
    //                 "archit@gmail.com",

    //             interviewerEmail:
    //                 "sarah@amazon.com",

    //             slotDate:
    //                 "2026-07-12",

    //             slotStartTime:
    //                 "18:00",

    //             slotEndTime:
    //                 "19:00"
    //         }

    //     ]

    // };

}

export async function createInterview(requestId , meetingLink ) {
    /*
        requestId will be passed as pathvariable and meetingLink as requestbody 
        {meetinglink}

    */

    console.log(`Recieved the interview request details with following requestId  ${requestId} and meeting Link ${meetingLink}`)


    const response = await api.put("/interviews/requests/"+requestId+"/accept",null,{
        params  : {
            meetingLink : meetingLink
        }
    })
    return response.data
    console.log("Interview Request Accepted")

    

    /*

        response fromat : 
    public record InterviewDTO(
    UUID interviewId, 
    String candidateEmail , 
    String interviewEmail,
    InterviewStatus interviewStatus , 
    String mettingLink,
    LocalDate interviewDate, 
    LocalTime interviewStartTime, 
    LocalTime interviewEndTime 


        ) {
            
        }
    */

//         return {

//     interviewId:
//         "123",

//     candidateEmail:
//         "archit@gmail.com",

//     interviewEmail:
//         "john@gmail.com",

//     interviewStatus:
//         "SCHEDULED",

//     meetingLink:
//         "https://meet.google.com/abc",

//     interviewDate:
//         "2026-07-10",

//     interviewStartTime:
//         "19:00",

//     interviewEndTime:
//         "20:00"

// };

}

export async function rejectInterviewRequest(requestId, rejectReason) {
    console.log(requestId)
  
    const response = await api.put("/interviews/requests/"+requestId+"/reject")
    /*
        Send the interviewRequestId as the pth variable
        "We get string as success"
        We hafe rejectReason as well
        change backend endpoint with reason
    */
   console.log("Backend doesnt take reason for now so lets log it here for now.    " + rejectReason)
   return response.data
   
}

export async function cancelInterviewRequest(requestId){
    console.log(requestId)


    const response = await api.put("/interviews/requests/"+requestId+"/cancel")
    /*
        Send the interviewRequestId as the pth variable
        "We get string as success"
      
    */


    console.log("Request reached server for cancelling the interview")
   return response.data
   
}


export async function completeInterview(interviewId) {

    const response = await api.put("/interviews/"+interviewId+"/complete")
    /*
Response : 
 UUID interviewId, 
    String candidateEmail , 
    String interviewEmail,
    InterviewStatus interviewStatus , 
    String mettingLink,
    LocalDate interviewDate, 
    LocalTime interviewStartTime, 
    LocalTime interviewEndTime 
    */

    return response.data

    console.log("Request received to mark the interview completed with interview id " + interviewId) 
    console.log("Interview Completed")

    
}

export async function cancelInterview(interviewId) {
    await api.put("/interviews/"+interviewId+"/cancel")
    console.log("Interview Cancellation Success") ; 


    
}

