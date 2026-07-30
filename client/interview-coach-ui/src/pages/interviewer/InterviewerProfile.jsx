import { useEffect, useState } from "react";

import { useParams } from "react-router-dom";

import ProfileCard from "../../components/user/ProfileCard"
import SkillsCard from "../../components/user/SkillsCard"
import ReadOnlyWorkExperienceCard from "../../components/user/ReadOnlyWorkExperienceCard"
import AvailableSlotsCard from "../../components/candidate/AvailableSlotsCard";

import { getProfileUsingEmail, getWorkExperienceUsingEmail } from "../../services/ProfileService";
import {getAvailableSlots} from "../../services/InterviewManagementService"

function InterviewerProfile() {

  const { email } = useParams();

  const [profile, setProfile] = useState({});
  const [workex, setWorkex] = useState([])
  const [slots, setSlots] = useState([])


  const loadSlots = async () => {
    try {
      const response = await getAvailableSlots(email)
      setSlots(response.slots)

    }
    catch (error) {
      console.error(error)
    }




  }
  const loadProfile =
    async () => {

      try {

        const response =
          await getProfileUsingEmail(
            email
          );

        setProfile(
          response.profile
        );

      } catch (error) {

        console.error(error);

      }

    };


  const loadWorkExperience = async () => {
    try {
      const response = await getWorkExperienceUsingEmail(email);
      setWorkex(response.workExperiences)
    }
    catch (error) {
      console.error(error)
    }
  }

  useEffect(() => {

    loadProfile();
    loadWorkExperience()
    loadSlots()


    console.log(profile)

  }, []);

  if (!profile || !workex || !slots) {

    return (
      <div>
        Loading...
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
                space-y-6
            "
        >

            <ProfileCard
                profile={profile}
            />

            <AvailableSlotsCard
                slots={slots}
                interviewerEmail={email}
            />

            <SkillsCard
                skills={
                    profile?.skills || []
                }
                editable={false}
            />

            <ReadOnlyWorkExperienceCard
                workExperiences={
                    workex
                }
            />

        </div>

    </div>
    

);

}

export default InterviewerProfile;