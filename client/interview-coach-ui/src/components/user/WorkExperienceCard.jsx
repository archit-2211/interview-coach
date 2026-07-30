import { useEffect, useState } from "react";

import {
  getWorkExperiences,
  createWorkExperience,
  deleteWorkExperience
} from "../../services/ProfileService";

function WorkExperienceCard() {

  const [workExperiences, setWorkExperiences] = useState([]);

  const [showForm, setShowForm] = useState(false);

  const [formData, setFormData] = useState({
    companyName: "",
    startDate: "",
    endDate: ""
  });

  const handleChange = (event) => {

    setFormData({
      ...formData,
      [event.target.name]: event.target.value
    });

  };

  const loadExperiences = async () => {

    try {

      const response =
        await getWorkExperiences();

      setWorkExperiences(
        response.workExperiences
      );

    } catch (error) {

      console.error(error);

    }

  };

  useEffect(() => {

    loadExperiences();

  }, []);

  const handleAddExperience = async (event) => {

    event.preventDefault();

    try {

      const response = await createWorkExperience({
        ...formData,
        endDate:
          formData.endDate || null
      });

      await loadExperiences()
      

      setFormData({
        companyName: "",
        startDate: "",
        endDate: ""
      });

      setShowForm(false);

    } catch (error) {

      console.error(error);

    }

  };

  const handleDelete = async (id) => {

    try {

      await deleteWorkExperience(id);

      await loadExperiences()

    } catch (error) {

      console.error(error);

    }

  };

  return (

    <div
      className="
        bg-white
        rounded-3xl
        shadow-xl
        border
        border-slate-200
        overflow-hidden
      "
    >

      <div
        className="
          px-8
          py-6
          border-b
          border-slate-200
          bg-slate-50
        "
      >

        <div
          className="
            flex
            justify-between
            items-center
            flex-wrap
            gap-4
          "
        >

          <div>

            <h2
              className="
                text-2xl
                font-bold
                text-slate-800
              "
            >
              Work Experience
            </h2>

            <p
              className="
                text-slate-500
                mt-1
              "
            >
              Showcase your professional journey
            </p>

          </div>

          <div
            className="
              flex
              items-center
              gap-3
            "
          >

            <span
              className="
                px-4
                py-2
                rounded-full
                bg-blue-100
                text-blue-700
                text-sm
                font-semibold
              "
            >
              {workExperiences.length}
              {" "}
              Experiences
            </span>

            <button
              onClick={() =>
                setShowForm(
                  !showForm
                )
              }
              className="
                px-5
                py-2
                rounded-xl
                bg-blue-600
                text-white
                font-medium
                hover:bg-blue-700
                transition
              "
            >
              {
                showForm
                  ? "Cancel"
                  : "Add Experience"
              }
            </button>

          </div>

        </div>

      </div>

      <div
        className="
          p-8
        "
      >

        {
          showForm && (

            <form
              onSubmit={
                handleAddExperience
              }
              className="
                bg-slate-50
                border
                border-slate-200
                rounded-2xl
                p-6
                mb-6
                space-y-4
              "
            >

              <input
                type="text"
                name="companyName"
                placeholder="Company Name"
                value={
                  formData.companyName
                }
                onChange={
                  handleChange
                }
                className="
                  w-full
                  border
                  border-slate-300
                  rounded-xl
                  px-4
                  py-3
                "
                required
              />

              <div
                className="
                  grid
                  md:grid-cols-2
                  gap-4
                "
              >

                <input
                  type="date"
                  name="startDate"
                  value={
                    formData.startDate
                  }
                  onChange={
                    handleChange
                  }
                  className="
                    border
                    border-slate-300
                    rounded-xl
                    px-4
                    py-3
                  "
                  required
                />

                <input
                  type="date"
                  name="endDate"
                  value={
                    formData.endDate
                  }
                  onChange={
                    handleChange
                  }
                  className="
                    border
                    border-slate-300
                    rounded-xl
                    px-4
                    py-3
                  "
                />

              </div>

              <button
                type="submit"
                className="
                  bg-blue-600
                  text-white
                  px-5
                  py-3
                  rounded-xl
                  font-medium
                  hover:bg-blue-700
                  transition
                "
              >
                Save Experience
              </button>

            </form>

          )
        }

        {
          workExperiences.length === 0
            ? (

              <div
                className="
                  bg-slate-50
                  border
                  border-dashed
                  border-slate-300
                  rounded-2xl
                  py-10
                  text-center
                "
              >

                <div
                  className="
                    text-4xl
                    mb-3
                  "
                >
                  💼
                </div>

                <p
                  className="
                    text-slate-600
                    font-medium
                  "
                >
                  No work experience added yet
                </p>

              </div>

            )
            : (

              <div
                className="
                  space-y-4
                "
              >

                {
                  workExperiences.map(
                    (experience) => (

                      <div
                        key={
                          experience.id
                        }
                        className="
                          border
                          border-slate-200
                          rounded-2xl
                          p-5
                          hover:shadow-md
                          transition
                        "
                      >

                        <div
                          className="
                            flex
                            justify-between
                            items-start
                          "
                        >

                          <div>

                            <h3
                              className="
                                text-lg
                                font-semibold
                                text-slate-800
                              "
                            >
                              {
                                experience.companyName
                              }
                            </h3>

                            <p
                              className="
                                text-slate-500
                                mt-2
                              "
                            >
                              {
                                experience.startDate
                              }
                              {" - "}
                              {
                                experience.endDate ||
                                "Present"
                              }
                            </p>

                          </div>

                          <button
                            onClick={() =>
                              
                              handleDelete(
                                experience.id
                              )
                            }
                            className="
                              px-4
                              py-2
                              rounded-xl
                              bg-red-50
                              text-red-600
                              hover:bg-red-100
                              transition
                            "
                          >
                            Delete
                          </button>

                        </div>

                      </div>

                    )
                  )
                }

              </div>

            )
        }

      </div>

    </div>

  );

}

export default WorkExperienceCard;