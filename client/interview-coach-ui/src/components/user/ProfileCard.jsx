function ProfileCard({ profile }) {

    const isVerified =
        profile.status ===
        "VERIFIED";

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
                    bg-gradient-to-r
                    from-blue-600
                    to-indigo-600
                    p-8
                    text-white
                "
            >

                <div
                    className="
                        flex
                        flex-col
                        md:flex-row
                        md:items-center
                        md:justify-between
                        gap-6
                    "
                >

                    <div>

                        <h2
                            className="
                                text-3xl
                                font-bold
                            "
                        >
                            {profile.name}
                        </h2>

                        <p
                            className="
                                text-blue-100
                                mt-2
                            "
                        >
                            {profile.email}
                        </p>

                        <div
                            className="
                                flex
                                gap-3
                                mt-4
                                flex-wrap
                            "
                        >

                            <span
                                className="
                                    px-4
                                    py-1.5
                                    rounded-full
                                    bg-white/20
                                    backdrop-blur-sm
                                    text-sm
                                    font-medium
                                "
                            >
                                {profile.role}
                            </span>

                            <span
                                className={`
                                    px-4
                                    py-1.5
                                    rounded-full
                                    text-sm
                                    font-medium
                                    ${
                                        isVerified
                                            ? "bg-emerald-500/20 text-emerald-100 border border-emerald-400/30"
                                            : "bg-amber-500/20 text-amber-100 border border-amber-400/30"
                                    }
                                `}
                            >
                                {profile.status}
                            </span>

                        </div>

                    </div>

                    <div
                        className="
                            bg-white/15
                            backdrop-blur-sm
                            border
                            border-white/20
                            rounded-2xl
                            px-6
                            py-4
                            min-w-[140px]
                        "
                    >

                        <p
                            className="
                                text-sm
                                text-blue-100
                            "
                        >
                            Rating
                        </p>

                        <div
                            className="
                                flex
                                items-center
                                gap-2
                                mt-1
                            "
                        >

                            <span
                                className="
                                    text-yellow-300
                                    text-xl
                                "
                            >
                                ⭐
                            </span>

                            <span
                                className="
                                    text-3xl
                                    font-bold
                                "
                            >
                                {profile.rating}
                            </span>

                        </div>

                    </div>

                </div>

            </div>

            <div
                className="
                    p-8
                "
            >

                <h3
                    className="
                        text-lg
                        font-semibold
                        text-slate-800
                        mb-5
                    "
                >
                    Personal Information
                </h3>

                <div
                    className="
                        grid
                        grid-cols-1
                        md:grid-cols-2
                        gap-5
                    "
                >

                    <InfoItem
                        label="Full Name"
                        value={profile.name}
                    />

                    <InfoItem
                        label="Email Address"
                        value={profile.email}
                    />

                    <InfoItem
                        label="Phone Number"
                        value={profile.phoneNumber}
                    />

                    <InfoItem
                        label="Role"
                        value={profile.role}
                    />

                </div>

            </div>

        </div>

    );
}

function InfoItem({
    label,
    value
}) {

    return (

        <div
            className="
                bg-slate-50
                hover:bg-slate-100
                border
                border-slate-200
                rounded-2xl
                p-5
                transition
            "
        >

            <p
                className="
                    text-xs
                    uppercase
                    tracking-wider
                    text-slate-500
                    font-medium
                    mb-2
                "
            >
                {label}
            </p>

            <p
                className="
                    text-slate-800
                    font-semibold
                    text-lg
                    break-all
                "
            >
                {value || "-"}
            </p>

        </div>

    );
}

export default ProfileCard;