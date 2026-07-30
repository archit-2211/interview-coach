import { useEffect, useState } from "react";
import { updateSkills } from "../../services/ProfileService";

function SkillsCard({
    skills = [],
    editable = true
}) {


    const [currentSkills, setCurrentSkills] = useState([]);

    const [savedSkills, setSavedSkills] = useState([]);

    const [newSkill, setNewSkill] = useState("");

    const [saving, setSaving] = useState(false);

    useEffect(() => {

        setCurrentSkills(
            [...skills]
        );

        setSavedSkills(
            [...skills]
        );

    }, [skills]);

    const handleAddSkill =
        (event) => {

            if (
                event.key === "Enter" &&
                newSkill.trim()
            ) {

                event.preventDefault();

                const skill =
                    newSkill.trim();

                const alreadyExists =
                    currentSkills.some(
                        currentSkill =>
                            currentSkill.toLowerCase() ===
                            skill.toLowerCase()
                    );

                if (alreadyExists) {

                    setNewSkill("");

                    return;

                }

                setCurrentSkills([
                    ...currentSkills,
                    skill
                ]);

                setNewSkill("");

            }

        };

    const handleDeleteSkill =
        (skillToDelete) => {

            setCurrentSkills(

                currentSkills.filter(
                    skill =>
                        skill !==
                        skillToDelete
                )

            );

        };

    const hasChanges =
        JSON.stringify(
            currentSkills
        ) !==
        JSON.stringify(
            savedSkills
        );


    const handleSave =
        async () => {

            try {

                setSaving(true);

                const response = await updateSkills(currentSkills)

                setSavedSkills(
                    [...currentSkills]
                );

            } finally {

                setSaving(false);

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
                            Skills
                        </h2>

                        <p
                            className="
                                text-slate-500
                                mt-1
                            "
                        >
                            Showcase your technical expertise
                        </p>

                    </div>

                    <div
                        className="
                            px-4
                            py-2
                            rounded-full
                            bg-blue-100
                            text-blue-700
                            font-semibold
                            text-sm
                        "
                    >
                        {currentSkills.length}
                        {" "}
                        Skills
                    </div>

                </div>

            </div>

            <div
                className="
                    p-8
                "
            >

                {
                    editable && (
                        <input
                            type="text"
                            value={newSkill}
                            placeholder="Add a skill and press Enter"
                            onChange={(event) =>
                                setNewSkill(
                                    event.target.value
                                )
                            }
                            onKeyDown={
                                handleAddSkill
                            }
                            className="
                w-full
                rounded-2xl
                border
                border-slate-300
                px-5
                py-4
                text-slate-700
                focus:outline-none
                focus:ring-2
                focus:ring-blue-500
                focus:border-transparent
                transition
            "
                        />
                    )
                }
                <div
                    className="
                        mt-6
                    "
                >

                    {
                        currentSkills.length === 0
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
                                        🚀
                                    </div>

                                    <p
                                        className="
                                            text-slate-600
                                            font-medium
                                        "
                                    >
                                        No skills added yet
                                    </p>

                                    <p
                                        className="
                                            text-sm
                                            text-slate-500
                                            mt-2
                                        "
                                    >
                                        Add skills to help others
                                        understand your expertise.
                                    </p>

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
                                        currentSkills.map(
                                            skill => (

                                                <div
                                                    key={skill}
                                                    className="
                                                        flex
                                                        items-center
                                                        gap-2
                                                        px-4
                                                        py-2.5
                                                        rounded-full
                                                        bg-gradient-to-r
                                                        from-blue-50
                                                        to-indigo-50
                                                        border
                                                        border-blue-200
                                                        text-slate-700
                                                        font-medium
                                                    "
                                                >

                                                    <span>
                                                        {skill}
                                                    </span>

                                                    {
                                                        editable && (
                                                            <button
                                                                onClick={() =>
                                                                    handleDeleteSkill(
                                                                        skill
                                                                    )
                                                                }
                                                                className="
                h-6
                w-6
                rounded-full
                flex
                items-center
                justify-center
                text-red-500
                hover:bg-red-100
                transition
            "
                                                            >

                                                            </button>
                                                        )
                                                    }

                                                </div>

                                            )
                                        )
                                    }

                                </div>

                            )
                    }

                </div>

                {
                    editable && (

                        <div
                            className="
                mt-8
                flex
                justify-end
            "
                        >

                            <button
                                onClick={
                                    handleSave
                                }
                                disabled={
                                    !hasChanges ||
                                    saving
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
                                {
                                    saving
                                        ? "Saving..."
                                        : "Save Changes"
                                }
                            </button>

                        </div>

                    )
                }

            </div>

        </div>

    );

}

export default SkillsCard;