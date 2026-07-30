import api from "./api"

export async function getProfile() {

    const response = await api.get("/profiles/me") 


    return response.data

    // await new Promise(
    //     resolve =>
    //         setTimeout(
    //             resolve,
    //             500
    //         )
    // );

    // return {

    //     email:
    //         "archit@gmail.com",

    //     name:
    //         "Archit Agarwal",

    //     phoneNumber:
    //         "9876543210",

    //     role:
    //         "CANDIDATE",

    //     status:
    //         "VERIFIED",

    //     rating:
    //         4.8,

    //     skills: [
    //         "Java",
    //         "Spring Boot",
    //         "Kafka",
    //         "Redis"
    //     ]

    // };

}


export async function updateSkills(skills) {

   const requestBody = {skills : skills}
   console.log("Got the request with skills")
   const response = await api.post("/profiles/me/skills", requestBody)
  
    console.log("Saving skills")

    return response.data

    // have to retrun list<string> directly here

}


export async function getWorkExperiences() {
    console.log("Request received at Get Work Experiences")

    /*

    api response format = {
                workExperiences  : [{id, companyName, startDate, endDate}, {id, companyName, startDate, endDate}]
    
    }

    */

    const response = await api.get("/profiles/me/work-experiences")
    return response.data



    // return {

    //     workExperiences: [

    //         {
    //             id: "1",
    //             companyName: "Fractal Analytics",
    //             startDate: "2025-03-01",
    //             endDate: null
    //         },

    //         {
    //             id: "2",
    //             companyName: "Amazon",
    //             startDate: "2024-01-15",
    //             endDate: "2025-02-28"
    //         },

    //         {
    //             id: "3",
    //             companyName: "Microsoft",
    //             startDate: "2023-06-01",
    //             endDate: "2024-01-10"
    //         },

    //         {
    //             id: "4",
    //             companyName: "Atlassian",
    //             startDate: "2022-01-01",
    //             endDate: "2023-05-31"
    //         }

    //     ]

    // };

}


export async function createWorkExperience(newExperience) {
    const requestBody = newExperience
    const response = await api.post("/profiles/me/work-experiences", requestBody)
    console.log(response.data)
    return response.data
    // const demo = {

    //     workExperiences: [

    //         {
    //             id: "1",
    //             companyName: "Fractal Analytics",
    //             startDate: "2025-03-01",
    //             endDate: null
    //         },

    //         {
    //             id: "2",
    //             companyName: "Amazon",
    //             startDate: "2024-01-15",
    //             endDate: "2025-02-28"
    //         },

    //         {
    //             id: "3",
    //             companyName: "Microsoft",
    //             startDate: "2023-06-01",
    //             endDate: "2024-01-10"
    //         },

    //         {
    //             id: "4",
    //             companyName: "Atlassian",
    //             startDate: "2022-01-01",
    //             endDate: "2023-05-31"
    //         }

    //     ]

    // };

    // console.log("request received for new Experience adding")
    // newExperience.id = 5
    // demo.workExperiences.push(newExperience)
    // return demo



}

export async function deleteWorkExperience(id) {
    console.log(id)
   const response = await api.delete("/profiles/me/work-experiences/"+id)
   
    // const demo = {

    //     workExperiences: [

    //         {
    //             id: 1,
    //             companyName: "Fractal Analytics",
    //             startDate: "2025-03-01",
    //             endDate: null
    //         },

    //         {
    //             id: 2,
    //             companyName: "Amazon",
    //             startDate: "2024-01-15",
    //             endDate: "2025-02-28"
    //         },

    //         {
    //             id: 3,
    //             companyName: "Microsoft",
    //             startDate: "2023-06-01",
    //             endDate: "2024-01-10"
    //         },

    //         {
    //             id: 4,
    //             companyName: "Atlassian",
    //             startDate: "2022-01-01",
    //             endDate: "2023-05-31"
    //         }

    //     ]

    // };
    // console.log("Got the request to delete workexperience with id " + id)
    // demo.workExperiences = demo.workExperiences.filter((ele) => ele.id != id)
    // console.log("Deletion complete")
    // return demo



}

export async function getProfileUsingEmail(email) {

    const response = await api.get("/profiles/interviewer", {
        params : {
            email : email
        }
    })

    return {profile : response.data}

    // return {
    //     profile: {

    //         email:
    //             "archit@gmail.com",

    //         name:
    //             "Archit Agarwal",

    //         phoneNumber:
    //             "9876543210",

    //         role:
    //             "CANDIDATE",

    //         status:
    //             "VERIFIED",

    //         rating:
    //             4.8,

    //         skills: [
    //             "Java",
    //             "Spring Boot",
    //             "Kafka",
    //             "Redis"
    //         ]
    //     }
    // };


}

export async function getWorkExperienceUsingEmail(email) {
    const response = await api.get("/profiles/work-experiences", {
        params : {
            email : email
        }
    })
    
    return response.data

    // return {

    //     workExperiences: [

    //         {
    //             id: "1",
    //             companyName: "Fractal Analytics",
    //             startDate: "2025-03-01",
    //             endDate: null
    //         },

    //         {
    //             id: "2",
    //             companyName: "Amazon",
    //             startDate: "2024-01-15",
    //             endDate: "2025-02-28"
    //         },

    //         {
    //             id: "3",
    //             companyName: "Microsoft",
    //             startDate: "2023-06-01",
    //             endDate: "2024-01-10"
    //         },

    //         {
    //             id: "4",
    //             companyName: "Atlassian",
    //             startDate: "2022-01-01",
    //             endDate: "2023-05-31"
    //         }

    //     ]

    // };


}