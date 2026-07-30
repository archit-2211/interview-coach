import { useState } from "react";
import { createInterviewRequest } from "../../services/InterviewManagementService";
import { useNavigate } from "react-router-dom";

function AvailableSlotsCard({
    slots = [], interviewerEmail
}) {

    const [topics, setTopics] = useState("");
    const [description, setDescription] = useState("");

    const [selectedSlotDetails, setSelectedSlotDetails] = useState(null);
    const [showConfirmation, setShowConfirmation] = useState(false);
    const navigate = useNavigate()
    const handleRequestInterview =
        () => {

            if (
                !selectedSlotDetails
            ) {
                return;
            }

            setShowConfirmation(
                true
            );

        };
    const handleConfirm = async () => {
        try {

            await createInterviewRequest(
                selectedSlotDetails.slotId,
                interviewerEmail,
                topics
                    .split(",")
                    .map(topic => topic.trim())
                    .filter(topic => topic.length > 0),
                description
            );

            setShowConfirmation(false);
            navigate("/interview-requests");

        } catch (error) {
            console.error(error);
        }
    };


    return (
        <>

            <div
                className="
                bg-white
                rounded-3xl
                shadow-xl
                border
                border-slate-200
                p-8
            "
            >

                <div
                    className="
                    flex
                    justify-between
                    items-center
                    mb-6
                "
                >

                    <h2
                        className="
                        text-2xl
                        font-bold
                        text-slate-800
                    "
                    >
                        Available Slots
                    </h2>

                    <span
                        className="
                        px-4
                        py-2
                        rounded-full
                        bg-green-100
                        text-green-700
                        text-sm
                        font-semibold
                    "
                    >
                        {slots.length}
                        {" "}
                        Slots
                    </span>

                </div>

                {
                    slots.length === 0
                        ? (

                            <div
                                className="
                                text-center
                                py-10
                                text-slate-500
                            "
                            >
                                No slots available
                            </div>

                        )
                        : (

                            <div
                                className="
        flex
        flex-wrap
        gap-3
    "
                            >

                                {
                                    slots.map(
                                        slot => (

                                            <button
                                                key={slot.slotId}
                                                onClick={() =>
                                                    setSelectedSlotDetails(
                                                        slot
                                                    )
                                                }
                                                className={`
                        px-4
                        py-3
                        rounded-full
                        border
                        transition
                        font-medium
                        ${selectedSlotDetails?.slotId === slot.slotId
                                                        ? "bg-blue-600 text-white border-blue-600"
                                                        : "bg-white text-slate-700 border-slate-300 hover:border-blue-400"
                                                    }
                    `}
                                            >

                                                {slot.date}
                                                {" | "}
                                                {slot.startTime}
                                                {" - "}
                                                {slot.endTime}

                                            </button>

                                        )
                                    )
                                }

                            </div>
                        )
                }

                <div
                    className="
                    mt-8
                    flex
                    justify-end
                "
                >

                    <button
                        onClick={
                            handleRequestInterview
                        }
                        disabled={
                            !selectedSlotDetails
                        }
                        className="
                        px-6
                        py-3
                        rounded-xl
                        bg-blue-600
                        text-white
                        font-semibold
                        hover:bg-blue-700
                        transition
                        disabled:bg-slate-400
                        disabled:cursor-not-allowed
                    "
                    >
                        Request Interview
                    </button>

                </div>

            </div>

            {
    showConfirmation && (

        <div
            className="
                fixed
                inset-0
                bg-black/50
                flex
                items-center
                justify-center
                z-50
            "
        >

            <div
                className="
                    bg-white
                    rounded-3xl
                    p-8
                    w-full
                    max-w-lg
                "
            >

                <h2 className="text-2xl font-bold mb-6">
                    Request Interview
                </h2>

                <div className="space-y-4">

                    <div>
                        <p className="font-semibold">
                            Date
                        </p>

                        <p>
                            {selectedSlotDetails?.date}
                        </p>
                    </div>

                    <div>
                        <p className="font-semibold">
                            Time
                        </p>

                        <p>
                            {selectedSlotDetails?.startTime}
                            {" - "}
                            {selectedSlotDetails?.endTime}
                        </p>
                    </div>

                    <div>
                        <label className="block mb-2 font-semibold">
                            Skills / Topics
                        </label>

                        <input
                            type="text"
                            value={topics}
                            onChange={(e) =>
                                setTopics(e.target.value)
                            }
                            placeholder="Java, Spring Boot, Kafka"
                            className="
                                w-full
                                border
                                rounded-lg
                                px-4
                                py-2
                            "
                        />
                    </div>

                    <div>
                        <label className="block mb-2 font-semibold">
                            Description
                        </label>

                        <textarea
                            rows="4"
                            value={description}
                            onChange={(e) =>
                                setDescription(e.target.value)
                            }
                            placeholder="Describe what you want help with..."
                            className="
                                w-full
                                border
                                rounded-lg
                                px-4
                                py-2
                            "
                        />
                    </div>

                </div>

                <div
                    className="
                        flex
                        justify-end
                        gap-3
                        mt-6
                    "
                >

                    <button
                        onClick={() =>
                            setShowConfirmation(false)
                        }
                        className="
                            px-4
                            py-2
                            border
                            rounded-lg
                        "
                    >
                        Cancel
                    </button>

                    <button
                        onClick={handleConfirm}
                        className="
                            px-4
                            py-2
                            bg-blue-600
                            text-white
                            rounded-lg
                        "
                    >
                        Create Request
                    </button>

                </div>

            </div>

        </div>

    )
}

        </>



    );

}

export default AvailableSlotsCard;