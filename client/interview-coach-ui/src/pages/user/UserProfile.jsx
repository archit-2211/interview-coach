import { useEffect, useState } from "react";

import ProfileCard from "../../components/user/ProfileCard"
import SkillsCard from "../../components/user/SkillsCard";
import WorkExperienceCard from "../../components/user/WorkExperienceCard";

import  {getProfile}  from "../../services/ProfileService" ; 

function UserProfile() {

    const [profile, setProfile] =useState(null);

    const [loading, setLoading] =useState(true);

    const [error, setError] =useState("");

    const loadProfile =
        async () => {

            try {

                setLoading(true);

                const response =
                    await getProfile();

                setProfile(
                    response
                );

            } catch (error) {

                console.error(
                    error
                );

                setError(
                    "Failed to load profile"
                );

            } finally {

                setLoading(false);

            }

        };

    useEffect(() => {

        loadProfile();

    }, []);

    if (loading) {

        return (

            <div
                className="
                    min-h-screen
                    flex
                    items-center
                    justify-center
                    text-slate-500
                "
            >
                Loading Profile...
            </div>

        );

    }

    if (error) {

        return (

            <div
                className="
                    min-h-screen
                    flex
                    items-center
                    justify-center
                    text-red-500
                "
            >
                {error}
            </div>

        );

    }

    return (

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
                        mb-8
                    "
                >

                    <h1
                        className="
                            text-4xl
                            font-bold
                            text-slate-800
                        "
                    >
                        My Profile
                    </h1>

                    <p
                        className="
                            text-slate-500
                            mt-2
                        "
                    >
                        Manage your profile,
                        skills and work experience.
                    </p>

                </div>

                <div
                    className="
                        space-y-6
                    "
                >

                    <ProfileCard
                        profile={profile}
                    />

                    <SkillsCard
                        skills={
                            profile.skills
                        }
                    />

                    <WorkExperienceCard />

                </div>

            </div>

        </div>

    );

}

export default UserProfile;