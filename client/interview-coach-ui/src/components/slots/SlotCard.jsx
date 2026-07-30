function SlotCard({slot,onDelete}) {

    return (

        <div
            className="
                bg-white
                rounded-2xl
                border
                border-slate-200
                p-5
                shadow-sm
                flex
                justify-between
                items-center
            "
        >

            <div>

                <p
                    className="
                        text-lg
                        font-semibold
                        text-slate-800
                    "
                >
                    {slot.date}
                </p>

                <p
                    className="
                        text-slate-500
                        mt-1
                    "
                >
                    {slot.startTime}
                    {" - "}
                    {slot.endTime}
                </p>

            </div>

            <button
                onClick={() =>
                    onDelete(
                        slot.slotId
                    )
                }
                className="
                    px-4
                    py-2
                    rounded-xl
                    bg-red-600
                    text-white
                    hover:bg-red-700
                    transition
                "
            >
                Delete
            </button>

        </div>

    );

}

export default SlotCard;