
import { useEffect, useState } from "react";

import SlotCard from "../../components/slots/SlotCard"

import { getMySlots, deleteSlot, createSlots } from "../../services/SlotServices"

function MySlots() {

  const [slots, setSlots] = useState([]);

  const [loading, setLoading] = useState(true);

  const [slotDetails, setSlotDetails] = useState([
    {
      date: "",
      startTime: "",
      endTime: ""
    }
  ]);

  const [
    showCreateSlotModal,
    setShowCreateSlotModal
] = useState(false);

  const addSlotRow =
    () => {

      setSlotDetails(
        previous => [

          ...previous,

          {
            date: "",
            startTime: "",
            endTime: ""
          }

        ]
      );

    };
  const removeSlotRow =
    (index) => {

      setSlotDetails(
        previous =>
          previous.filter(
            (_, currentIndex) =>
              currentIndex !== index
          )
      );

    };

  const updateSlot =
    (
      index,
      field,
      value
    ) => {

      const updatedSlots =
        [...slotDetails];

      updatedSlots[index] = {

        ...updatedSlots[index],

        [field]: value

      };

      setSlotDetails(
        updatedSlots
      );

    };

  const handleCreateSlots =
    async () => {

      const response = await createSlots({

        slots:
          slotDetails

      });
      await loadSlots()
      setSlotDetails([
        {
          date: "",
          startTime: "",
          endTime: ""
        }
      ]);
      setShowCreateSlotModal(
        false
      );



    };

  const loadSlots =
    async () => {

      try {

        const response =
          await getMySlots();

        setSlots(
          response.slots
        );

      } catch (error) {

        console.error(
          error
        );

      } finally {

        setLoading(
          false
        );

      }

    };



  const handleDelete =
    async (slotId) => {

      try {

        await deleteSlot(
          slotId
        );

        setSlots(
          previousSlots =>
            previousSlots.filter(
              slot =>
                slot.slotId !==
                slotId
            )
        );

      } catch (error) {

        console.error(
          error
        );

      }

    };

  useEffect(() => {

    loadSlots();

  }, []);

  return (
    <>

      <div
        className="
                min-h-screen
                bg-slate-100
                py-10
                px-4
            "
      >

        <div
          className="
                    max-w-6xl
                    mx-auto
                "
        >

          <div
            className="
                        flex
                        justify-between
                        items-center
                        mb-8
                    "
          >

            <div>

              <h1
                className="
                                text-4xl
                                font-bold
                                text-slate-800
                            "
              >
                My Slots
              </h1>

              <p
                className="
                                text-slate-500
                                mt-2
                            "
              >
                Manage your
                interview
                availability.
              </p>

            </div>

            <button
              onClick={() => {setShowCreateSlotModal(true)}}
              className="
                            px-5
                            py-3
                            rounded-xl
                            bg-blue-600
                            text-white
                            font-semibold
                            hover:bg-blue-700
                            transition
                        "
            >
              Add Slot
            </button>

          </div>

          {
            loading
              ? (

                <div
                  className="
                                    text-center
                                    py-20
                                    text-slate-500
                                "
                >
                  Loading...
                </div>

              )
              : slots.length === 0
                ? (

                  <div
                    className="
                                        bg-white
                                        rounded-3xl
                                        border
                                        border-slate-200
                                        p-12
                                        text-center
                                        text-slate-500
                                    "
                  >
                    No Slots Found
                  </div>

                )
                : (

                  <div
                    className="
                                        space-y-4
                                    "
                  >

                    {
                      slots.map(
                        slot => (

                          <SlotCard
                            key={
                              slot.slotId
                            }
                            slot={
                              slot
                            }
                            onDelete={
                              handleDelete
                            }
                          />

                        )
                      )
                    }

                  </div>

                )
          }

        </div>

      </div>

      {
    showCreateSlotModal && (

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
                    max-w-4xl
                "
            >

                <h2
                    className="
                        text-2xl
                        font-bold
                        mb-6
                    "
                >
                    Create Slots
                </h2>

                <div
                    className="
                        space-y-4
                    "
                >

                    {
                        slotDetails.map(
                            (
                                slot,
                                index
                            ) => (

                                <div
                                    key={index}
                                    className="
                                        grid
                                        grid-cols-4
                                        gap-3
                                    "
                                >

                                    <input
                                        type="date"
                                        value={slot.date}
                                        onChange={(event) =>
                                            updateSlot(
                                                index,
                                                "date",
                                                event.target.value
                                            )
                                        }
                                        className="
                                            border
                                            rounded-xl
                                            px-3
                                            py-2
                                        "
                                    />

                                    <input
                                        type="time"
                                        value={slot.startTime}
                                        onChange={(event) =>
                                            updateSlot(
                                                index,
                                                "startTime",
                                                event.target.value
                                            )
                                        }
                                        className="
                                            border
                                            rounded-xl
                                            px-3
                                            py-2
                                        "
                                    />

                                    <input
                                        type="time"
                                        value={slot.endTime}
                                        onChange={(event) =>
                                            updateSlot(
                                                index,
                                                "endTime",
                                                event.target.value
                                            )
                                        }
                                        className="
                                            border
                                            rounded-xl
                                            px-3
                                            py-2
                                        "
                                    />

                                    <button
                                        onClick={() =>
                                            removeSlotRow(
                                                index
                                            )
                                        }
                                        className="
                                            bg-red-600
                                            text-white
                                            rounded-xl
                                        "
                                    >
                                        Delete
                                    </button>

                                </div>

                            )
                        )
                    }

                </div>

                <button
                    onClick={
                        addSlotRow
                    }
                    className="
                        mt-4
                        px-4
                        py-2
                        rounded-xl
                        bg-slate-200
                    "
                >
                    + Add Row
                </button>

                <div
                    className="
                        mt-8
                        flex
                        justify-end
                        gap-3
                    "
                >

                    <button
                        onClick={() =>
                            setShowCreateSlotModal(
                                false
                            )
                        }
                        className="
                            px-4
                            py-2
                            border
                            rounded-xl
                        "
                    >
                        Cancel
                    </button>

                    <button
                        onClick={
                            handleCreateSlots
                        }
                        className="
                            px-4
                            py-2
                            bg-blue-600
                            text-white
                            rounded-xl
                        "
                    >
                        Create Slots
                    </button>

                </div>

            </div>

        </div>

    )
}
    </>

  );

}

export default MySlots;
