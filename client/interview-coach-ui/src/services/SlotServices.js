import api from "./api";

export async function getMySlots() {
    const response = await api.get("http://localhost:8080/slots/me")
    console.log(response.data)
    return response.data


    // return {

    //     slots: [

    //         {
    //             slotId: "1",
    //             date: "2026-07-15",
    //             startTime: "18:00",
    //             endTime: "19:00"
    //         },

    //         {
    //             slotId: "2",
    //             date: "2026-07-16",
    //             startTime: "20:00",
    //             endTime: "21:00"
    //         },

    //         {
    //             slotId: "3",
    //             date: "2026-07-18",
    //             startTime: "09:00",
    //             endTime: "10:00"
    //         }

    //     ]

    // };

}
export async function deleteSlot(slotId) {
    console.log("Sending delete request")
    await api.delete("/slots/"+slotId)
}

export async function createSlots(allSlots) {
    const response = await api.post("http://localhost:8080/slots/me" , allSlots)
    

    /*
        Pass Request like List<SlotDTO> we get the same response
    */

    // console.log("Creating slots with details" )
    // console.log(allSlots) 

    return response.data
}