function ReadOnlyWorkExperienceCard({
    workExperiences = []
}) {

    return (

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
                    Work Experience
                </h2>

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

            </div>

            {
                workExperiences.length === 0
                    ? (

                        <div
                            className="
                                text-center
                                py-8
                                text-slate-500
                            "
                        >
                            No work experience available
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
                                    experience => (

                                        <div
                                            key={
                                                experience.id
                                            }
                                            className="
                                                border
                                                border-slate-200
                                                rounded-2xl
                                                p-5
                                                bg-slate-50
                                            "
                                        >

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

                                    )
                                )
                            }

                        </div>

                    )
            }

        </div>

    );

}

export default ReadOnlyWorkExperienceCard;