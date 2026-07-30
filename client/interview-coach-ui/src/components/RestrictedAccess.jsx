import { Link } from "react-router-dom";

function RestrictedAccess() {

    return (

        <div
            className="
                min-h-screen
                bg-slate-100
                flex
                items-center
                justify-center
                px-4
            "
        >

            <div
                className="
                    bg-white
                    rounded-3xl
                    shadow-xl
                    border
                    border-slate-200
                    p-10
                    max-w-lg
                    text-center
                "
            >

                <div
                    className="
                        text-6xl
                        mb-6
                    "
                >
                    🔒
                </div>

                <h1
                    className="
                        text-3xl
                        font-bold
                        text-slate-800
                        mb-4
                    "
                >
                    Access Restricted
                </h1>

                <p
                    className="
                        text-slate-600
                        leading-relaxed
                        mb-8
                    "
                >
                    You do not have permission to
                    access this page. If you believe
                    this is a mistake, please contact
                    the administrator.
                </p>

                <Link
                    to="/profile/me"
                    className="
                        inline-flex
                        items-center
                        justify-center
                        px-6
                        py-3
                        rounded-xl
                        bg-blue-600
                        text-white
                        font-medium
                        hover:bg-blue-700
                        transition
                    "
                >
                    Go To Profile
                </Link>

            </div>

        </div>

    );

}

export default RestrictedAccess;